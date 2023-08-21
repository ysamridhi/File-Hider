package views;

import dao.UserDAO;
import model.User;
import service.GenerateOTP;
import service.SendOTPService;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

public class Welcome {
    public void welcomeScreen(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the App");
        System.out.println("Press 1 : Login");
        System.out.println("Press 2: SignUp");
        System.out.println("Press 0: Exit");
        int choice = 0;
        try{
            choice = Integer.parseInt(br.readLine());

        }catch (IOException e){
            e.printStackTrace();
        }
        switch (choice){
            case 1 -> login();
            case 2 -> signUp();
            case 0 -> System.exit(0);
        }

    }

    private void signUp() {
        Scanner scn = new Scanner(System.in);
        System.out.println("Enter name :");
        String name = scn.nextLine();
        System.out.println("Enter Email :");
        String email = scn.nextLine();
        String genOTP = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email,genOTP);
        System.out.println("Enter the OTP");
        String otp = scn.nextLine();
        if(otp.equals(genOTP)){
            User user = new User(name,email);
            int response = UserService.saveUser(user);
            switch(response){
                case 0 -> System.out.println("user registered");
                case 1-> System.out.println("User Already exits");
            }
        }else{
            System.out.println("Wrong OTP");
        }


    }

    private void login() {
        Scanner scn = new Scanner(System.in);
        System.out.println("Enter Email : ");
        String email = scn.nextLine();
        try{
            if(UserDAO.isExist(email)){
                String genOTP = GenerateOTP.getOTP();
                SendOTPService.sendOTP(email,genOTP);
                System.out.println("Enter the OTP");
                String otp = scn.nextLine();
                if(otp.equals(genOTP)){
                    new UserView(email).home();

                }else{
                    System.out.println("Wrong OTP");
                }
            }else{
                System.out.println("User Not Found");
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
}
