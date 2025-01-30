package com.example.firebasetodoapp.frag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.firebasetodoapp.AuthState
import com.example.firebasetodoapp.AuthViewModel
import com.example.firebasetodoapp.R
import com.example.firebasetodoapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch


class loginFrag : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private val auth : AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        auth.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Authenticated -> {
                    findNavController().navigate(R.id.action_loginFrag_to_menuFrag)
                }

                is AuthState.Error -> {
                    binding.tvError.text = state.message
                }

                else -> {}
            }
        }


        binding.etUsername.setOnClickListener {
            binding.tvError.text = "";
        }

        binding.etPassword.setOnClickListener {
            binding.tvError.text = "";
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            auth.login(email, password)

            lifecycleScope.launch {
                Thread.sleep(2000)
                if (auth.authState.value == AuthState.Authenticated){
                    findNavController().navigate(R.id.action_loginFrag_to_menuFrag)
                }
                else if (auth.authState.value is AuthState.Error){
                    val errorMessage = (auth.authState.value as AuthState.Error).message
                    binding.tvError.text = errorMessage
                }

            }


        }


        binding.signUpTxt.setOnClickListener {
            findNavController().navigate(R.id.action_loginFrag_to_signUpFrag)
        }

    }

}


