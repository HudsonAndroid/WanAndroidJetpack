package com.hudson.wanandroid.ui.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.hudson.wanandroid.R
import com.hudson.wanandroid.data.entity.wrapper.BaseResult
import com.hudson.wanandroid.databinding.FragmentRegisterBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.ui.util.showToast
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
                val registerResult: BaseResult? = accountModel.register()
                if(registerResult?.isSuccess() == true){
                    showToast(R.string.tips_register_success)
                    // back to login page
                    Navigation.findNavController(binding.root).navigateUp()
                }else{
                    val errorMsg = registerResult?.errorMsg
                    showToast(if(errorMsg == null || errorMsg.isEmpty()) getString(R.string.tips_register_failed) else errorMsg)
                }
            }
        }
        binding.tvLogin.setOnClickListener{
            Navigation.findNavController(binding.root).navigateUp()
        }
    }

}
