package com.controller;

import com.model.User;
import com.model.dto.FilterDto;
import com.model.dto.StatisticsDto;
import com.service.StatisticsService;
import com.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/statistics")
@Tag(name = "Statistics Controller", description = "Endpoints for handling requests for Statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    private final UserService userService;

    @Operation(summary = "Endpoint to get number and sum of expenses/incomes by month and year")
    @PostMapping("/totalSumAndNumber")
    public ResponseEntity<List<StatisticsDto>> getNumberAndSumOfTransactionsByMonthAndYear(@RequestBody FilterDto filterDto) {
        User user = this.userService.findByUsername(filterDto.getUsername());
        List<StatisticsDto> list = this.statisticsService.getNumberAndSumOfTransactionsByMonthAndYear(user, filterDto.getYear());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to get total sum of expenses/incomes by month and year")
    @PostMapping("/totalSum")
    public ResponseEntity<StatisticsDto> getTotalSumOfTransactionsByMonthAndYear(@RequestBody FilterDto filterDto) {
        User user = this.userService.findByUsername(filterDto.getUsername());
        StatisticsDto statisticsDto = this.statisticsService.getTotalSumOfTransactionsByMonthAndYear(user, filterDto.getYear());

        return new ResponseEntity<>(statisticsDto, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to get percentage of expenses by category, month and year")
    @PostMapping("/percentageOfExpenses")
    public ResponseEntity<List<Double>> getPercentagesOfExpensesByCategory(@RequestBody StatisticsDto statisticsDto) {
        User user = this.userService.findByUsername(statisticsDto.getUsername());
        List<Double> doubles = this.statisticsService.getStatisticsForExpensesByCategory(user, statisticsDto);

        return new ResponseEntity<>(doubles, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint to get percentage of incomes by category, month and year")
    @PostMapping("/percentageOfIncomes")
    public ResponseEntity<List<Double>> getPercentagesOfIncomesByCategory(@RequestBody StatisticsDto statisticsDto) {
        User user = this.userService.findByUsername(statisticsDto.getUsername());
        List<Double> doubles = this.statisticsService.getStatisticsForIncomesByCategory(user, statisticsDto);

        return new ResponseEntity<>(doubles, HttpStatus.OK);
    }
}