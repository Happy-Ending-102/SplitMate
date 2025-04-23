// File: src/main/java/controller/Controller.java
package controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import model.*;
import service.*;
import repository.FriendshipRepository;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @PostMapping
    public User createUser(@RequestBody User dto) {
        return userService.registerUser(dto);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User dto) {
        dto.setId(id);
        return userService.updateUser(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.removeUser(id);
    }
}

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired private GroupService groupService;

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable String id) {
        return groupService.getGroup(id);
    }

    @PostMapping
    public Group createGroup(@RequestBody Group dto) {
        return groupService.createGroup(dto);
    }

    @PostMapping("/{gId}/members/{uId}")
    public Group addMember(@PathVariable String gId, @PathVariable String uId) {
        return groupService.addUserToGroup(gId, uId);
    }

    @DeleteMapping("/{gId}/members/{uId}")
    public Group removeMember(@PathVariable String gId, @PathVariable String uId) {
        return groupService.removeUserFromGroup(gId, uId);
    }
}

@RestController
@RequestMapping("/groups/{gId}/expenses")
public class ExpenseController {
    @Autowired private ExpenseService expenseService;

    @PostMapping
    public Expense addExpense(@PathVariable String gId, @RequestBody Expense dto) {
        return expenseService.addExpense(gId, dto);
    }

    @GetMapping
    public List<Expense> listExpenses(@PathVariable String gId) {
        return expenseService.listExpenses(gId);
    }
}

@RestController
@RequestMapping("/groups/{gId}/payments")
public class PaymentController {
    @Autowired private PaymentCalculator calculator;
    @Autowired private GroupService groupService;
    @Autowired private FriendshipRepository friendshipRepo;

    @GetMapping
    public List<Payment> calculatePayments(@PathVariable String gId) {
        Group g = groupService.getGroup(gId);
        List<Friendship> fs = g.getMembers().stream()
            .flatMap(u -> friendshipRepo.findByUserAOrUserB(u, u).stream())
            .collect(Collectors.toList());
        return calculator.calculate(g, fs);
    }
}

@RestController
@RequestMapping("/convert")
public class CurrencyController {
    @Autowired private CurrencyConverter converter;

    @GetMapping
    public BigDecimal convert(
        @RequestParam BigDecimal amount,
        @RequestParam Currency from,
        @RequestParam Currency to
    ) {
        return converter.convert(amount, from, to);
    }
}
