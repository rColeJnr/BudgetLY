package com.rick.budgetly.feature_bills.domain.use_case

import com.rick.budgetly.feature_bills.domain.Bill
import com.rick.budgetly.feature_bills.domain.IBillRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

class GetBills(
    private val repository: IBillRepository
){
    operator fun invoke(): Flow<List<Bill>> =
        repository.getBills()
}