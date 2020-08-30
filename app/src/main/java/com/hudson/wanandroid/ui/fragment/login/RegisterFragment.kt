package com.hudson.wanandroid.ui.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.hudson.wanandroid.databinding.FragmentRegisterBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.viewmodel.AccountModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterFragment : Fragment(), Injectable {

    private var binding by autoCleared<FragmentRegisterBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val accountModel: AccountModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.accountModel = accountModel
        binding.lifecycleOwner = this
        binding.btnRegister.setOnClickListener{
            lifecycleScope.launch {
                val registerResult = accountModel.register()
                if(registerResult.isSuccess()){
                    Toast.makeText(context, "register success", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context, registerResult.errorMsg, Toast.LENGTH_LONG).show()
                }
            }
        }
        binding.tvLogin.setOnClickListener{
            Navigation.findNavController(binding.root).navigateUp()
        }
    }

}
