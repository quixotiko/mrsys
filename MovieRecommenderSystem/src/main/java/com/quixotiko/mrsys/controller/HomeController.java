package com.quixotiko.mrsys.controller;

import com.quixotiko.mrsys.domain.Rating;
import com.quixotiko.mrsys.domain.RatingId;
import com.quixotiko.mrsys.domain.RecommendResult;
import com.quixotiko.mrsys.domain.UserRating;
import com.quixotiko.mrsys.repository.RecommendResultDao;
import com.quixotiko.mrsys.repository.UserRatingDao;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    @Resource
    private UserRatingDao userRatingDao;
    @Resource
    private RecommendResultDao recommendResultDao;
    private static final Log logger = LogFactory.getLog(HomeController.class);
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "词频统计应用");
        return "index";
    }
    @GetMapping("/recommendmovieforuser")
    public String handleSubmitRec(@RequestParam("userid") String userid, Model model) throws IOException, InterruptedException {
        String jarPath = "/home/hadoop/IdeaProjects/ALSModel/out/artifacts/ALSModel_jar/ALSModel.jar";
        String ratingDir = "hdfs://localhost:9000/rating-part.dat";

        executeCommand("/usr/local/spark/bin/spark-submit", "--class", "com.quixotiko.Main", "--jars","/usr/local/spark/jars/mysql-connector-j-8.1.0/mysql-connector-j-8.1.0.jar", jarPath, ratingDir, userid);
        List<RecommendResult> results = recommendResultDao.findByIdUserid(Integer.parseInt(userid));
        logger.info(results);
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            resultList.add("Result :" + results.get(i).getId().getMovieid() + "-" + results.get(i).getRating() + '\n');
            logger.info(resultList);
        }
        model.addAttribute("results", resultList.toString());
        model.addAttribute("userid", userid);
        // 读取统计结果

        return "recommendresult";

    }

    @PostMapping("/submituserscore")
    public String handleSubmit(@RequestParam String userid,
                               @RequestParam List<String> movieid,
                               @RequestParam List<String> moviescore,
                               Model model) {
        logger.info(moviescore);
        for (int i = 0; i < movieid.size(); i++) {
            int mid = Integer.parseInt(movieid.get(i));
            int ms = Integer.parseInt(moviescore.get(i));
            RatingId id = new RatingId();
            id.setUserid(Long.parseLong(userid));
            id.setMovieid(mid);
            UserRating rating = new UserRating();
            rating.setId(id);
            rating.setRating(ms);
            rating.setTimestamp(System.currentTimeMillis());
            userRatingDao.save(rating);
        }
        model.addAttribute("userid", userid);
        return "submitrecommend";
    }
//    private String executeCommand(String... command) throws IOException {
//        ProcessBuilder processBuilder = new ProcessBuilder(command);
//        processBuilder.redirectErrorStream(true);
//        Process process = processBuilder.start();
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//            List<String> lines = reader.lines().collect(Collectors.toList());
//            logger.info(lines);
//            return String.join("\n", lines);
//        } catch (IOException e) {
//            throw new IOException("执行命令时发生错误", e);
//        }
//    }
private String executeCommand(String... command) throws IOException, InterruptedException {
    ProcessBuilder processBuilder = new ProcessBuilder(command);
    processBuilder.redirectErrorStream(true);
    Process process = processBuilder.start();

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        List<String> lines = reader.lines().collect(Collectors.toList());
        logger.info(lines);
        return String.join("\n", lines);
    } catch (IOException e) {
        throw new IOException("执行命令时发生错误", e);
    } finally {
        // 等待 Spark 任务完成
        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            throw new InterruptedException();
        }
    }
}
}
