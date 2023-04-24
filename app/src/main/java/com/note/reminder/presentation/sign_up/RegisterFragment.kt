package com.note.reminder.presentation.sign_up


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.note.reminder.R
import com.note.reminder.common.ui.BaseFragment
import com.note.reminder.databinding.FragmentRegisterBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val viewModel : SignUpViewModel by viewModels()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(layoutInflater,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiEvent()
        handleButtonClicks()
        listenToTextChange()

    }
    private fun listenToTextChange() {
        binding.apply {
            etEmail.addTextChangedListener {
                viewModel.onEvent(SignUpEvent.EmailChanged(it.toString()))
            }
            etPassword.addTextChangedListener {
                viewModel.onEvent(SignUpEvent.PasswordChanged(it.toString()))
            }
            etPasswordRepeat.addTextChangedListener {
                viewModel.onEvent(SignUpEvent.RepeatPasswordChanged(it.toString()))
            }
        }
    }
    private fun observeUiEvent(){
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.signUpEvent.collect{ event ->
                when(event){
                    is SignUpViewModel.UiSignUpEvent.NavigateToNoteListScreen -> {
                        findNavController().apply {
                            popBackStack()
                            navigate(Uri.parse("noteApp://noteList"))
//                            navigate(R.id.action_loginFragment_to_noteListFragment)
                        }
                    }
                    is SignUpViewModel.UiSignUpEvent.NavigateToLoginScreen -> {
                        findNavController().popBackStack()
//                        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                    }
                    is SignUpViewModel.UiSignUpEvent.ShowSnackbar -> {
                        Snackbar.make(
                            requireView(),
                            event.message.asString(requireActivity()),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    is SignUpViewModel.UiSignUpEvent.ShowProgressBar -> {
                        binding.progressBar.isVisible = event.isLoading
                    }
                }
            }
        }
    }
    private fun handleButtonClicks() {
        binding.btnGoToLogin.setOnClickListener {
            viewModel.onEvent(SignUpEvent.BackToLoginButtonClicked)
        }

        binding.btnRegister.setOnClickListener {
            viewModel.onEvent(SignUpEvent.RegisterUserButtonClicked)
        }
//        binding.btnRegister.setOnClickListener {
//            findNavController().navigate(
//                R.id.action_registerFragment_to_noteListFragment
//            )
//        }
    }
}