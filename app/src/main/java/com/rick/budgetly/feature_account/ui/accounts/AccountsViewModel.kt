package com.rick.budgetly.feature_account.ui.accounts

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.rick.budgetly.feature_account.domain.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(): ViewModel() {

    internal val _accountsState = mutableStateOf(AccountsState())
    internal val accountsState: State<AccountsState> = _accountsState

    internal var lastDeletedAccount: Account? = null

}

/*
* because as much as you may not likeTo admit, idid get quiter
* keyboard, and supposingly moch comforable
* than the previous one, i just odnt dont kf the size
* will come to be a problm wiith egonormics but as far as placing it in aconfortable place
* that wotnbe a probwm
* plus the keys are layout jsut like the notebook keys
* what is dope and more confortable to which between keyboard
* also it chicklet keys, what means that the typing experience
* is shit faced, but yeah, 1550, fuck me.
* */