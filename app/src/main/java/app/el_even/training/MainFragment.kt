package app.el_even.training

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.el_even.training.databinding.MainFragmentBinding
import timber.log.Timber

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<MainFragmentBinding>(
            inflater,
            R.layout.main_fragment,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = CoinAdapter()
        binding.mainRv.adapter = adapter
        viewModel.coins.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

}