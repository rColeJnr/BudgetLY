package com.rick.budgetly.feature_account

import android.app.Application
import androidx.room.Room
import com.rick.budgetly.feature_account.common.ProductionDispatcherProvider
import com.rick.budgetly.feature_account.domain.IAccountRepository
import com.rick.budgetly.feature_account.domain.use_case.*
import com.rick.budgetly.feature_account.persistence.AccountDatabase
import com.rick.budgetly.feature_account.persistence.AccountDatabase.Companion.DATABASE_NAME
import com.rick.budgetly.feature_account.persistence.AccountRepositoryImpl
import com.rick.budgetly.feature_account.ui.accounts.AccountsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AccountModule {

    @Provides
    @Singleton
    fun providesAccountDatabase(app: Application): AccountDatabase =
        Room.databaseBuilder(
            app,
            AccountDatabase::class.java,
            DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun providesAccountRepository(database: AccountDatabase): IAccountRepository =
        AccountRepositoryImpl(database.accountDao)

    @Provides
    @Singleton
    fun providesAccountUseCases(repository: IAccountRepository): AccountUseCases  = AccountUseCases(
        getAccounts = GetAccounts(repository),
        getAccountsByType = GetAccountByType(repository),
        getAccountById = GetAccountById(repository),
        saveAccount = SaveAccount(repository ),
        deleteAccount = DeleteAccount(repository)
    )

    @Provides
    @Singleton
    fun providesAccountDispatchers() =
        ProductionDispatcherProvider
}