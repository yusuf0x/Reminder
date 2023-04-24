package com.note.reminder.presentation.sign_in

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
import com.note.reminder.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel : SignInViewModel by viewModels()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) =  FragmentLoginBinding.inflate(layoutInflater,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiEvent()
        handleButtonClicks()
        listenToTextChange()

    }
    private fun listenToTextChange() {
        binding.apply {
            etEmail.addTextChangedListener {
                viewModel.onEvent(SignInEvent.EmailChanged(it.toString()))
            }
            etPassword.addTextChangedListener {
                viewModel.onEvent(SignInEvent.PasswordChanged(it.toString()))
            }
        }
    }
    private fun observeUiEvent(){
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.singInEvent.collect{ event ->
                when(event){
                    is SignInViewModel.UiSignInEvent.NavigateToNoteListScreen -> {
                        findNavController().apply {
                            popBackStack()
                            navigate(Uri.parse("noteApp://noteList"))
//                            navigate(R.id.action_loginFragment_to_noteListFragment)
                        }
                    }
                    is SignInViewModel.UiSignInEvent.NavigateToRegisterScreen -> {
                        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                    }
                    is SignInViewModel.UiSignInEvent.ShowSnackbar -> {
                        Snackbar.make(
                            requireView(),
                            event.message.asString(requireActivity()),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    is SignInViewModel.UiSignInEvent.ShowProgressBar -> {
                        binding.progressBar.isVisible = event.isLoading
                    }
                }
            }
        }
    }
    private fun handleButtonClicks() {
        binding.apply {
            btnNewAccount.setOnClickListener {
                viewModel.onEvent(SignInEvent.RegisterNewAccountButtonClicked)
            }

            btnLogin.setOnClickListener {
                viewModel.onEvent(SignInEvent.LoginButtonClicked)
            }
        }
    }

}