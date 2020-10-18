package com.appl.controller;

import com.appl.dao.Repo;
import com.appl.model.Day;
import com.appl.openWeather.OpenWeatherRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.List;
import java.util.Map;

@Controller
public class HelloController {

    @Autowired
    private Repo repo;

    @GetMapping("/t")
    public String showTable(Map<String, Object> model) {

        List<Day> listDays = repo.findAll();
        model.put("days", listDays);

        int rows = listDays.size();
        model.put("rowsCounter", rows);

        return "index";
    }

    @GetMapping("/gt")
    public String getTable(Map<String, Object> model) {
        OpenWeatherRequest owr = new OpenWeatherRequest();
        owr.getWeather();
        List<Day> listDays = repo.findAll();
        model.put("days", listDays);
        return "index";
    }


}