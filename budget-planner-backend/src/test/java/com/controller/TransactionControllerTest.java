package com.controller;

import com.model.Transaction;
import com.model.User;
import com.model.dto.TransactionDto;
import com.model.dto.FilterDto;
import com.service.TransactionService;
import com.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;
    
    @Mock
    private TransactionService transactionService;

    @Mock
    private UserService userService;

    private Transaction transaction;

    private TransactionDto transactionDto;

    private User user;

    private List<Transaction> transactions;

    private FilterDto filterDto;

    @BeforeEach
    public void setup() {
        transaction = Transaction.builder().id(1L).build();
        user = User.builder().build();
        transactions = new ArrayList<>();
        transactionDto = TransactionDto.builder().build();
        filterDto = FilterDto.builder().build();
    }

    @Test
    public void getAllTransactionsTest() {
        // given
        when(transactionService.getAll()).thenReturn(transactions);

        // when
        ResponseEntity<List<Transaction>> response = transactionController.getAllTransactions();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getTransactionTest() {
        // given
        when(transactionService.getTransactionById(1L)).thenReturn(transaction);

        // when
        ResponseEntity<Transaction> response = transactionController.getTransaction(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void addTransactionTest() {
        // given
        when(userService.findByUsername(transactionDto.getUsername())).thenReturn(user);
        when(transactionService.addTransaction(transactionDto, user)).thenReturn(transaction);

        // when
        ResponseEntity<Transaction> response = transactionController.addTransaction(transactionDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateTransactionTest() {
        // given
        when(transactionService.update(1L, transactionDto)).thenReturn(transaction);

        // when
        ResponseEntity<Transaction> response = transactionController.updateTransaction(1L, transactionDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteTransactionTest() {
        // given
        when(transactionService.delete(1L)).thenReturn(1L);

        // when
        ResponseEntity<Long> response = transactionController.deleteTransaction(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void filterTransactionsTest() {
        // given
        when(userService.findByUsername(filterDto.getUsername())).thenReturn(user);
        when(transactionService.filterTransactions(user, filterDto)).thenReturn(transactions);

        // when
        ResponseEntity<List<Transaction>> response = transactionController.filterTransactions(filterDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void filterByUserTest() {
        // given
        when(userService.findByUsername(filterDto.getUsername())).thenReturn(user);
        when(transactionService.getTransactionsByUser(user)).thenReturn(transactions);

        // when
        ResponseEntity<List<Transaction>> response = transactionController.filterByUser(filterDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void filterByDateTest() {
        // given
        when(userService.findByUsername(filterDto.getUsername())).thenReturn(user);
        when(transactionService.getTransactionsByDate(user,filterDto)).thenReturn(transactions);

        // when
        ResponseEntity<List<Transaction>> response = transactionController.filterByDate(filterDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


}
