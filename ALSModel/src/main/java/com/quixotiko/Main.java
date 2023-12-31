package com.quixotiko;

import org.apache.spark.SparkConf;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.ml.tuning.CrossValidator;
import org.apache.spark.ml.tuning.CrossValidatorModel;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.Properties;

import static org.apache.spark.sql.functions.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("ALSModel").setMaster("local");
        SparkSession spark = SparkSession.builder().appName("ALSModel").config(conf).getOrCreate();

        String ratingDir = args[0];
        int userid = Integer.parseInt(args[1]);
//        String ratingDir = "hdfs://localhost:9000/rating-part.dat";
        StructType ratingsDFSchema = new StructType()
                .add(DataTypes.createStructField("userid", DataTypes.IntegerType, false))
                .add(DataTypes.createStructField("movieid", DataTypes.IntegerType, false))
                .add(DataTypes.createStructField("rating", DataTypes.IntegerType, true))
                .add(DataTypes.createStructField("timestamp", DataTypes.LongType, true));

        Dataset<Row> ratingsDF = spark.read()
                .option("delimiter", "::")
                .option("header", false)
                .schema(ratingsDFSchema)
                .csv(ratingDir)
                .toDF("userid", "movieid", "rating", "timestamp");

//        ratingsDF.show();

        Dataset<Row> userRatingsDF = spark
                .read().format("jdbc")
                .option("url", "jdbc:mysql://localhost:3306/mrsys")
                .option("driver", "com.mysql.cj.jdbc.Driver")
                .option("dbtable", "userratings")
                .option("user", "root")
                .option("password", "Password123#@!").load();

        Dataset<Row> currentUserRatingsDF = userRatingsDF.filter("userid="+userid);

//        userRatingsDF.show();
        Dataset<Row> trainDF = ratingsDF.union(currentUserRatingsDF);
//        trainDF.orderBy(desc("userid")).show();
//        trainDF.createOrReplaceTempView("traindf");
//        trainDF.show();
//        Dataset<Row> temp = spark.sql("select * from traindf where userid=6042");
//        temp.show();
        ALS als = new ALS()
                .setMaxIter(10)
                .setRegParam(0.1)
                .setImplicitPrefs(false)
                .setUserCol("userid")
                .setItemCol("movieid")
                .setRatingCol("rating");

        ParamMap[] paramGrid = new ParamGridBuilder()
                .addGrid(als.rank(), new int[]{8})
                .addGrid(als.regParam(), new double[]{0.01,0.1})
                .addGrid(als.maxIter(), new int[]{5})
                .build();

        RegressionEvaluator evaluator = new RegressionEvaluator()
                .setMetricName("rmse")
                .setLabelCol("rating")
                .setPredictionCol("prediction");

        CrossValidator cv = new CrossValidator()
                .setEstimator(als)
                .setEvaluator(evaluator)
                .setEstimatorParamMaps(paramGrid)
                .setNumFolds(2)
                .setParallelism(2);

        CrossValidatorModel cvModel = cv.fit(trainDF);
//        ALSModel alsModel = als.fit(trainDF);
//        Dataset<Row> predictions = alsModel.transform(trainDF);
        ALSModel bestModel = (ALSModel) cvModel.bestModel();
        Dataset<Row> userRecs = bestModel.recommendForAllUsers(10);
        userRecs.show(false);
        Dataset<Row> recommendResultDF = userRecs
                .select("userid", "recommendations")
                .withColumn("recommendation", functions.explode(col("recommendations")))
                .select(
                        col("userid"),
                        col("recommendation.movieid").as("movieid"),
                        col("recommendation.rating").as("rating"),
                        functions.current_timestamp().cast("long").as("timestamp")
                ).filter("userid = "+userid);
        recommendResultDF.show();

//        Dataset<Row> recommendResultDF = spark.sql("select * from predictions where userid=6042 order by rating limit 10");

        Properties props = new Properties();
        props.put("user", "root");
        props.put("password", "Password123#@!");
        props.put("driver", "com.mysql.cj.jdbc.Driver");

        recommendResultDF.write()
                .mode("overwrite").jdbc("jdbc:mysql://localhost:3306/mrsys", "mrsys.recommendresult", props);

        }
}