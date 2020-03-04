package org.rewedigital.katana.android.example.fragment.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_second.view.*
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.android.example.R
import org.rewedigital.katana.android.example.fragment.SecondFragmentModule
import org.rewedigital.katana.android.example.fragment.inject.Container
import org.rewedigital.katana.android.example.fragment.model.SecondFragmentViewModel
import org.rewedigital.katana.android.fragment.KatanaFragment
import org.rewedigital.katana.androidx.viewmodel.savedstate.viewModelSavedStateNow
import org.rewedigital.katana.injectNow

/**
 * See [FirstFragment] first.
 * This is an alternative approach to the Fragment/Activity relation issue.
 *
 * Also this fragment showcases Katana's support for [androidx.lifecycle.ViewModel].
 *
 * @see FirstFragment
 * @see SecondFragmentModule
 * @see KatanaFragment
 */
class SecondFragment(
    private val superComponent: Component
) : KatanaFragment(), KatanaTrait {

    override lateinit var component: Component
    private lateinit var container: Container
    private lateinit var viewModel: SecondFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_second, container, false).apply {
            button.setOnClickListener {
                viewModel.message = editText.text?.toString()
                updateMessage()
            }
        }

    override fun onInject(activity: Activity, savedInstanceState: Bundle?) {
        component = superComponent + SecondFragmentModule

        container = injectNow()
        viewModel = viewModelSavedStateNow()

        updateMessage()
    }

    private fun updateMessage() {
        val message = StringBuilder().apply {
            append(
                context?.getString(
                    R.string.second_fragment,
                    container.activityDependency,
                    container.fragmentDependency
                )
            )

            append(" ${viewModel.message.orEmpty()}")
        }.trim()

        view?.textView?.text = message
    }
}
