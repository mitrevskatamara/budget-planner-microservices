package com.service;

import com.messages.NotificationMessage;
import com.model.*;
import com.model.dto.TransactionDto;
import com.model.enumerations.NotificationType;
import com.model.exceptions.ResourceNotFoundException;
import com.repository.TransactionRepository;
import com.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    private static final long ID = 1L;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BudgetService budgetService;

    @Mock
    private TransactionTypeService transactionTypeService;

    @Mock
    private RabbitMqService rabbitMqService;

    private Transaction transaction;

    private User user;

    private List<Transaction> transactions;

    private TransactionDto transactionDto;

    private Budget budget;

    private TransactionType transactionType;

    @BeforeEach
    public void setup() {
        budget = Budget.builder().month(Month.JANUARY).balance(1.0).build();
        user = User.builder().build();
        transactionType = TransactionType.builder().build();
        transaction = Transaction.builder().transactionType(transactionType).date(LocalDate.now()).user(user).build();
        transactions = new ArrayList<>();
        transactionDto = TransactionDto.builder().date(LocalDate.now()).build();
    }

    @Test
    public void getTransactionByIdSuccessfullyTest() {
        // given
        when(transactionRepository.findById(ID)).thenReturn(Optional.of(transaction));

        // when
        Transaction transactionFromDb = transactionService.getTransactionById(ID);

        // then
        verify(transactionRepository).findById(any());
        assertThat(transactionFromDb).isNotNull();
        assertThat(transactionFromDb).isEqualTo(transaction);
    }

    @Test
    public void getTransactionByIdUnsuccessfullyTest() {
        // given
        when(transactionRepository.findById(ID))
                .thenThrow(new ResourceNotFoundException("Transaction", "id", String.valueOf(ID)));

        // when

        // then
        assertThatThrownBy(() -> transactionService.getTransactionById(ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .message().isEqualTo("Transaction not found with id : '1'");
    }

    @Test
    public void getAllTransactionTest() {
        // given
        List<Transaction> transactionList = new ArrayList<>();
        when(transactionRepository.findAll()).thenReturn(transactionList);

        // when
        List<Transaction> transactions = transactionService.getAll();

        // then
        verify(transactionRepository).findAll();
        assertThat(transactions).isNotNull();
    }

    @Test
    public void addTransactionSuccessfullyTest() {
        // given
        when(transactionTypeService.findByCategoryAndType(transactionDto.getCategoryId(), transactionDto.getType()))
                .thenReturn(transactionType);
        LocalDate date = transactionDto.getDate();
        when(budgetService.existsByMonthAndYear(date.getMonth(), date.getYear(), user)).thenReturn(true);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(budgetService.updateBudgetAfterAddTransaction(transaction)).thenReturn(budget);

        // when
        Transaction savedTransaction = transactionService.addTransaction(transactionDto, user);

        // then
        verify(budgetService).updateBudgetAfterAddTransaction(transaction);
        assertThat(savedTransaction).isNotNull();
    }

    @Test
    public void updateTransactionTest() {
        // given
        when(transactionRepository.findById(ID)).thenReturn(Optional.ofNullable(transaction));
        when(transactionTypeService.findByCategoryAndType(transactionDto.getCategoryId(), transactionDto.getType()))
                .thenReturn(transactionType);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        // when
        Transaction updatedTransaction = transactionService.update(ID, transactionDto);

        // then
        verify(transactionRepository).findById(ID);
        verify(transactionTypeService).findByCategoryAndType(any(), any());
        verify(transactionRepository).save(transaction);
        assertThat(updatedTransaction).isNotNull();
    }

    @Test
    public void deleteTransactionTest() {
        // given
        NotificationMessage notificationMessage = NotificationMessage.builder()
                .notificationType(NotificationType.TRANSACTION_DELETE)
                .month(Month.JANUARY.getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                .build();
        when(transactionRepository.findById(ID)).thenReturn(Optional.of(transaction));
        when(budgetService.updateBudgetAfterDeleteTransaction(transaction)).thenReturn(budget);
        willDoNothing().given(transactionRepository).delete(transaction);
        willDoNothing().given(rabbitMqService).sendMessageToNotificationService(notificationMessage);

        // when
        Long deletedTransactionId = transactionService.delete(ID);

        // then
        verify(transactionRepository).delete(transaction);
        assertThat(deletedTransactionId).isNull();
    }

    @Test
    public void deleteBudgetThrowExceptionTest() {
        // given
        when(transactionRepository.findById(any()))
                .thenThrow(new ResourceNotFoundException("Transaction", "id", String.valueOf(ID)));

        // when

        // then
        assertThatThrownBy(() -> transactionService.getTransactionById(any()))
                .isInstanceOf(ResourceNotFoundException.class)
                .message().isEqualTo("Transaction not found with id : '1'");
    }
}
