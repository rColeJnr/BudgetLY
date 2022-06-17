@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.rick.add_edit

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rick.add_edit.components.AccountAddEditDetails
import com.rick.budgetly.calculator.Calculator
import com.rick.budgetly_components.AnimatedIconRow
import com.rick.budgetly_components.BackPressHandler
import com.rick.budgetly_components.BaseBottomSheet
import com.rick.budgetly_components.DefaultInputText
import com.rick.core.BudgetLyContainer
import com.rick.data.AccountColor
import com.rick.data.AccountCurrency
import com.rick.data.AccountIcon
import com.rick.data.AccountType
import com.rick.screen_add_edit.R
import com.rick.util.TestTags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AccountAddEditBody(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: AccountAddEditViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is BudgetLyContainer.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is BudgetLyContainer.ShowSuccess ->
                    navController.navigateUp()
                else -> {}
            }
        }
    }

    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    BaseBottomSheet(
        state = state,
        controlNavigation = {
            BackPressHandler {
                if (state.isVisible) {
                    scope.launch { state.hide() }
                } else {
                    navController.navigateUp()
                }
            }
        },
        sheetContent = {
            Calculator(
                viewModel.calculatorValue.value
            ) {
                viewModel.onEvent(AccountAddEditEvents.CalculatorEvent(it))
            }
        }
    ) {
        ScreenContent(modifier, viewModel, navController, state, scope)
        if (!state.isVisible) viewModel.calculatorValue.value = ""
    }
}

@ExperimentalMaterialApi
@Composable
private fun ScreenContent(
    modifier: Modifier,
    viewModel: AccountAddEditViewModel,
    navController: NavHostController,
    state: ModalBottomSheetState,
    scope: CoroutineScope,
) {
    val iconList = mutableListOf<ImageVector>()
    for (icon in AccountIcon.values()) {
        iconList.add(icon.imageVector)
    }
    Column(
        modifier = modifier
            .fillMaxHeight(),
    ) {
        TopBarWithTextField(
            iconsVisible = viewModel.accountTitle.value.isNotEmpty(),
            text = viewModel.accountTitle.value,
            onTextChange = { viewModel.onEvent(AccountAddEditEvents.EnteredTitle(it)) },
            icon = AccountIcon.values()[viewModel.accountIcon.value].imageVector,
            iconList = iconList,
            onIconChange = { viewModel.onEvent(AccountAddEditEvents.ChangeAccountIcon(it)) },
            color = AccountColor.values()[viewModel.accountColor.value],
            onColorChange = { viewModel.onEvent(AccountAddEditEvents.ChangeAccountColor(it.color)) },
            onSaveAccount = { viewModel.onEvent(AccountAddEditEvents.SaveAccount) },
            onCancelAccount = { viewModel.onEvent(AccountAddEditEvents.CancelAccount); navController.navigateUp() })

        AccountAddEditDetails(
            type = viewModel.accountType.value,
            onTypeChange = { viewModel.onEvent(AccountAddEditEvents.ChangeAccountType(AccountType.values()[it].type)) },
            currency = viewModel.accountCurrency.value,
            onCurrencyChange = {
                viewModel.onEvent(
                    AccountAddEditEvents.ChangeAccountCurrency(
                        AccountCurrency.values()[it].currency
                    )
                )
            },
            checked = viewModel.accountInTotalStatus.value,
            onCheckedChange = { viewModel.onEvent(AccountAddEditEvents.ChangeIncludeInTotalStatus(it)) },
            description = viewModel.accountDescription.value,
            onDescriptionChange = { viewModel.onEvent(AccountAddEditEvents.EnteredDescription(it)) },
            limit = viewModel.accountLimit.value,
            onLimitClick = {
                viewModel.calculatorValue.value = viewModel.accountLimit.value
                viewModel.calculateLimit.value = true
                scope.launch { state.show() }
            },
            balance = viewModel.accountBalance.value,
            onBalanceClick = {
                viewModel.calculatorValue.value = viewModel.accountBalance.value
                viewModel.calculateLimit.value = false
                scope.launch { state.show() }
            },
            scope = scope,
            state = state,
        )
    }
}

@Composable
fun TopBarWithTextField(
    iconsVisible: Boolean,
    text: String,
    onTextChange: (String) -> Unit,
    iconList: List<ImageVector>,
    icon: ImageVector,
    onIconChange: (Int) -> Unit,
    color: AccountColor,
    onColorChange: (AccountColor) -> Unit,
    onSaveAccount: () -> Unit,
    onCancelAccount: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Cancel,
                contentDescription = stringResource(id = R.string.cancel),
                modifier = Modifier
                    .clickable { onCancelAccount() }
                    .size(34.dp)
                    .padding(start = 4.dp)
            )
            Text(text = stringResource(R.string.new_account), style = MaterialTheme.typography.h5)
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = stringResource(id = R.string.save),
                modifier = Modifier
                    .clickable { onSaveAccount() }
                    .size(34.dp)
                    .padding(end = 4.dp)
            )
        }
        DefaultInputText(
            text = text,
            onTextChange = onTextChange,
            label = stringResource(R.string.name),
            testTag = TestTags.newAccountTitle
        )
        if (iconsVisible) {
            Column {
                // I should have just one animated row
                AnimatedIconRow(
                    iconList = iconList,
                    icon = icon,
                    onIconChange = onIconChange,
                    modifier = Modifier.padding(top = 8.dp)
                )
                // TODO to be implement after i complete base functionality of app
//                AnimatedColorsRow(
//                    colorsVisible = false,
//                    color = color,
//                    onColorChange = onColorChange
//                )
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
fun PrevScreenContent() {

}

@Preview
@Composable
fun PrevTopBar() {

}
