package org.rewedigital.katana.android.example.fragment.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_first.view.*
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.android.example.R
import org.rewedigital.katana.android.example.fragment.secondFragmentModule
import org.rewedigital.katana.createComponent

/**
 * @see FirstFragment
 */
class SecondFragment : Fragment(),
                       KatanaTrait {

    override val component: Component by lazy {
        createComponent(
            dependsOn = listOf((activity as KatanaTrait).component),
            modules = listOf(secondFragmentModule)
        )
    }

    private lateinit var fragmentDependency: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_second, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fragmentDependency = component.injectNow("FRAGMENT_DEPENDENCY2")

        view?.textView?.text = context?.getString(R.string.second_fragment, fragmentDependency)
    }
}
