package controller;

import model.Customer;
import service.CustomerService;
import service.ICustomerService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CustomerController", urlPatterns = "/index.jsp")
public class CustomerController extends HttpServlet {
    ICustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        if (action.equals("")) {
            showCustomers(request, response);
        } else if (action.equals("edit")) {
            editCustomer(request, response);
        } else if (action.equals("delete")) {
            deleteCustomer(request, response);
        }
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) {
        customerService.removeById(request.getParameter("id"));
        request.removeAttribute("id");
        request.removeAttribute("action");
        showCustomers(request, response);
    }

    private void editCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/create.jsp");
        request.setAttribute("name", customerService.findById(request.getParameter("id")).getName());
        request.setAttribute("id", request.getParameter("id"));
        request.setAttribute("email", customerService.findById(request.getParameter("id")).getEmail());
        request.setAttribute("address", customerService.findById(request.getParameter("id")).getAddress());
        dispatcher.forward(request, response);
    }

    private void showCustomers(HttpServletRequest request, HttpServletResponse response) {
        List<Customer> customers = customerService.findAll();
        request.setAttribute("customers", customers);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/list.jsp");
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        RequestDispatcher dispatcher;
        try {
            Customer customer = new Customer(id, name, email, address);
            customerService.save(customer);
        } catch (Exception e) {
            dispatcher = request.getRequestDispatcher("/customer/error-404.jsp");
            dispatcher.forward(request, response);
        }
        request.removeAttribute("action");
        showCustomers(request, response);
    }

}
