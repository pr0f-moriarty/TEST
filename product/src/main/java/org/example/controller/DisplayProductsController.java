package org.example.controller;

import org.example.util.FileUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DisplayProductsController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> products = FileUtil.readProducts();
      response.setContentType("text/plain");
        for (String product : products) {
            response.getWriter().println(product);
        }
    }
}
