package homework;

import homework.config.HibernateConfig;
import homework.dao.UserDao;
import homework.dao.UserDaoImpl;
import homework.entity.User;
import homework.service.UserService;
import org.hibernate.SessionFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final SessionFactory sessionFactory = HibernateConfig.getSessionFactory();
    private static final UserDao dao = new UserDaoImpl(sessionFactory);
    private static final UserService userService = new UserService(dao);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1 - показать всех пользователей");
            System.out.println("2 - показать пользователя по id");
            System.out.println("3 - добавить пользователя");
            System.out.println("4 - обновить пользователя");
            System.out.println("5 - удалить пользователя");
            System.out.println("0 - Выход");
            System.out.print("Выберите действие: ");

            String choiceInput = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(choiceInput);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 0 до 5.");
                continue;
            }
            if (choice == 0) {
                break;
            }

            switch (choice) {
                case 1:
                    showAllUsers();
                    break;
                case 2:
                    System.out.println("Введите id пользователя");
                    String idInput = scanner.nextLine().trim();
                    long id = Long.parseLong(idInput);
                    showUserById(id);
                    break;
                case 3:
                    User user = inputData(scanner);
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    user.setCreatedAt(now);
                    userService.addUser(user);
                    break;
                case 4:
                    System.out.println("Введите id пользователя для обновления:");
                    String updIdInput = scanner.nextLine().trim();
                    long updId = Long.parseLong(updIdInput);
                    User updUser = inputData(scanner);
                    updUser.setId(updId);
                    userService.updateUser(updUser);
                    System.out.println("Пользователь обновлён: " + updUser.getName());
                    break;
                case 5:
                    System.out.println("Введите id пользователя для удаления:");
                    String delIdInput = scanner.nextLine().trim();
                    long delId = Long.parseLong(delIdInput);
                    userService.deleteUser(delId);
                    System.out.println("Пользователь с ID " + delId + " удалён.");
                    break;
                default:
                    System.out.println("Ошибка: выберите действие от 0 до 5.");
            }
        }
    }

    private static User inputData(Scanner scanner) {
        System.out.println("Введите имя пользователя:");
        String name = scanner.nextLine().trim();

        System.out.println("Введите email пользователя:");
        String email = scanner.nextLine().trim();

        System.out.println("Введите возраст пользователя:");
        int age = Integer.parseInt(scanner.nextLine().trim());

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        return user;
    }

    private static void showAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("Пользователи не найдены.");
        } else {
            System.out.println("------------------------------");
            System.out.println("Список пользователей:");
            for (User user : users) {
                outputUserData(user);
            }
            System.out.println("------------------------------");
        }
    }

    private static void showUserById(long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            System.out.println("Пользователь с ID " + id + " не найден.");
        } else {
            System.out.println("------------------------------");
            outputUserData(user);
            System.out.println("------------------------------");
        }
    }

    private static void outputUserData(User user) {
        System.out.println("Пользователь: ID: " + user.getId() +
                ", Name: " + user.getName() +
                ", Email: " + user.getEmail() +
                ", Age: " + user.getAge() +
                ", CreatedAt: " + user.getCreatedAt());
    }

}
