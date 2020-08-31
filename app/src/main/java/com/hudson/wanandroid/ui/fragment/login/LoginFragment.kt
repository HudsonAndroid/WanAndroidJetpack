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
import com.hudson.wanandroid.databinding.FragmentLoginBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.ui.util.showToast
import com.hudson.wanandroid.viewmodel.AccountModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class LoginFragment : Fragment(), Injectable {
    private var binding by autoCleared<FragmentLoginBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val accountModel: AccountModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.accountModel = accountModel
        binding.lifecycleOwner = this
        binding.btnLogin.setOnClickListener{
            lifecycleScope.launch {
                val login = accountModel.login()
                if(login.isSuccess()){
                    requireActivity().finish()
                    showToast(R.string.tips_login_success)
                }else{
                    showToast(if(login.errorMsg.isEmpty()) getString(R.string.tips_login_failed) else login.errorMsg)
                }
            }
        }
        binding.tvRegister.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.registerFragment)
        }
    }

}
