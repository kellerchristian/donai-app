package com.donai.app.di

import com.donai.app.screens.login.LoginViewModel
import com.donai.app.domain.usecase.LoginUseCase
import com.donai.app.screens.activeRequest.ActiveRequestsViewModel
import com.donai.app.screens.completeProfile.CompleteProfileViewModel
import com.donai.app.screens.confirmed.DonationConfirmedViewModel
import com.donai.app.screens.createAccount.CreateAccountViewModel
import com.donai.app.screens.createRequest.CreateRequestViewModel
import com.donai.app.screens.dashboard.HomeViewModel
import com.donai.app.screens.elegibility.EligibilityViewModel
import com.donai.app.screens.history.DonationHistoryViewModel
import com.donai.app.screens.profile.ProfileViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val viewModelModule = module {

    viewModel {
        LoginViewModel(
            loginUseCase = get<LoginUseCase>()
        )
    }
    viewModel {
        CreateAccountViewModel()
    }
    viewModel{
        CompleteProfileViewModel()
    }
    viewModel {
        HomeViewModel()
    }
    viewModel {
        ActiveRequestsViewModel()
    }
    viewModel {
        EligibilityViewModel()
    }
    viewModel {
        DonationConfirmedViewModel()
    }
    viewModel {
        CreateRequestViewModel()
    }
    viewModel{
        DonationHistoryViewModel()
    }
    viewModel{
        ProfileViewModel()
    }

}