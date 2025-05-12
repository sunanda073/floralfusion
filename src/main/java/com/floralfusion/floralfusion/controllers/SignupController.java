package com.floralfusion.floralfusion.controllers;

import com.floralfusion.floralfusion.entities.Collector;
import com.floralfusion.floralfusion.entities.Company;
import com.floralfusion.floralfusion.entities.Customer;
import com.floralfusion.floralfusion.entities.DeliveryPartner;
import com.floralfusion.floralfusion.entities.FlowerContributor;
import com.floralfusion.floralfusion.entities.User;
import com.floralfusion.floralfusion.repositories.CollectorRepository;
import com.floralfusion.floralfusion.repositories.CompanyRepository;
import com.floralfusion.floralfusion.repositories.CustomerRepository;
import com.floralfusion.floralfusion.repositories.DeliveryPartnerRepository;
import com.floralfusion.floralfusion.repositories.FlowerContributorRepository;
import com.floralfusion.floralfusion.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class SignupController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectorRepository collectorRepository;

    @Autowired
    private FlowerContributorRepository flowerContributorRepository;

    @Autowired
    private CompanyRepository companyRepresentativeRepository;

    @Autowired
    private DeliveryPartnerRepository deliveryPartnerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/signup")
    public String handleSignup(
            @RequestParam("userType") String userType,
            @RequestParam("fname") String fname,
            @RequestParam("lname") String lname,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("password") String password,
            @RequestParam("country") String country,
            @RequestParam("state") String state,
            @RequestParam("city") String city,
            @RequestParam(value = "type", required = false) String contributorType,
            @RequestParam(value = "area", required = false) String area,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "companyAddress", required = false) String companyAddress,
            @RequestParam(value = "position", required = false) String position,
            @RequestParam(value = "companyPhone", required = false) String companyPhone,
            @RequestParam(value = "website", required = false) String website,
            @RequestParam(value = "taxId", required = false) String taxId) {

        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            return "redirect:/signup?error=emailExists";
        }

        // Save to User table
        User user = new User();
        user.setFname(fname);
        user.setLname(lname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setRole(userType);
        user.setCountry(country);
        user.setState(state);
        user.setCity(city);
        user = userRepository.save(user);

        // If user is a contributor, save to flower_contributors
        if ("contributor".equalsIgnoreCase(userType) && contributorType != null) {
            FlowerContributor contributor = new FlowerContributor();
            contributor.setUser(user);
            contributor.setType(contributorType);
            contributor.setArea(area);
            flowerContributorRepository.save(contributor);
        }

        // Save Company Representative
        if ("company".equalsIgnoreCase(userType)) {
            Company company = new Company();
            company.setUser(user);
            company.setCompanyName(companyName);
            company.setCompanyAddress(companyAddress);
            company.setPosition(position);
            company.setCompanyPhone(companyPhone);
            company.setWebsite(website);
            company.setTaxID(taxId);
            companyRepresentativeRepository.save(company);
        }

        // New block for customer
        if ("customer".equalsIgnoreCase(userType)) {
            Customer customer = new Customer();
            customer.setUser(user);
            customerRepository.save(customer);
        }

        return "redirect:/login?success=Registration successful. Please log in.";
    }

    @PostMapping("/user/login")
    public String loginUser(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session) {

        Optional<User> userOptional = userRepository.findByEmailAndPassword(email, password);

        if (userOptional.isEmpty()) {
            return "redirect:/login?error=true";
        }

        User user = userOptional.get();
        session.setAttribute("user", user);

        // Redirect based on user role
        switch (user.getRole().toLowerCase()) {
            case "contributor":
                FlowerContributor contributor = flowerContributorRepository.findByUser(user);
                if (contributor != null) {
                    session.setAttribute("contributorId", contributor.getContributorID());
                }
                return "redirect:/contributor";

            case "collector":
                Collector collector = collectorRepository.findByUser(user);
                if (collector != null) {
                    session.setAttribute("collector", collector);
                }
                return "redirect:/collectors/collector";

            case "company":
                Company company = companyRepresentativeRepository.findByUser_UserID(user.getUserID());
                if (company != null) {
                    session.setAttribute("companyId", company.getCompanyID());
                    session.setAttribute("companyName", company.getCompanyName());
                    session.setAttribute("company", company);
                }
                return "redirect:/company";

            case "delivery_partner":
                DeliveryPartner deliveryPartner = deliveryPartnerRepository.findByUser(user);
                if (deliveryPartner != null) {
                    session.setAttribute("deliveryPartnerId", deliveryPartner.getDeliveryPartnerId());
                    session.setAttribute("deliveryPartner", deliveryPartner);
                }
                return "redirect:/delivery-partners/dashboard";

            case "customer":
                Customer customer = customerRepository.findByUser(user);
                if (customer != null) {
                    session.setAttribute("customerId", customer.getCustomerID());
                    session.setAttribute("customer", customer);
                }
                return "redirect:/customer";

            default:
                return "redirect:/dashboard";
        }
    }
}
