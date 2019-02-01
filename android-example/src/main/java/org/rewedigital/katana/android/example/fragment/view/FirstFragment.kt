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
import org.rewedigital.katana.android.example.fragment.firstFragmentModule
import org.rewedigital.katana.createComponent

class FirstFragment : Fragment(),
                      KatanaTrait {

    // Component must be initialized lazily because it has a dependency to parent Activity
    override val component: Component by lazy {
        createComponent(
            dependsOn = listOf((activity as KatanaTrait).component),
            modules = listOf(firstFragmentModule)
        )
    }

    private lateinit var activityDependency: String
    private lateinit var fragmentDependency: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_first, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Injection must happen here, after Activity was created!
        // Accessing `component` before this lifecycle method will result in an error.

        activityDependency = component.injectNow("SOME_DEPENDENCY")
        fragmentDependency = component.injectNow("FRAGMENT_DEPENDENCY1")

        view?.textView?.text =
            context?.getString(R.string.first_fragment, activityDependency, fragmentDependency)
    }
}
