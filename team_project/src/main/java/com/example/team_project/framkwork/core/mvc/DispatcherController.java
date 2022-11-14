package com.example.team_project.framkwork.core.mvc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//接管所有请求
//@WebServlet(urlPatterns = "/")
public class DispatcherController extends HttpServlet {

    private List<ControllerInfo<?>> controllerInfos;

    public DispatcherController() {

    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        super.service(req, resp);


    }

    private boolean RequestToController(String url) {
        return false;
    }

}
