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
import org.rewedigital.katana.android.example.fragment.FRAGMENT_DEPENDENCY1
import org.rewedigital.katana.android.example.fragment.SOME_DEPENDENCY
import org.rewedigital.katana.android.example.fragment.firstFragmentModule
import org.rewedigital.katana.android.fragment.KatanaFragmentDelegate
import org.rewedigital.katana.android.fragment.fragmentDelegate
import org.rewedigital.katana.injectNow

/**
 * Showcasing usage of Katana in a Fragment which has a dependency relation to the parent Activity.
 * Since the Activity is instantiated **after** the Fragment instance, we need to take special care
 * of this situation.
 *
 * @see KatanaFragmentDelegate
 * @see SecondFragment
 */
class FirstFragment : Fragment(),
                      KatanaTrait {

    private val fragmentDelegate: KatanaFragmentDelegate<FirstFragment>

    override lateinit var component: Component
    private lateinit var activityDependency: String
    private lateinit var fragmentDependency: String

    init {
        fragmentDelegate = fragmentDelegate { activity ->
            component = (activity as KatanaTrait).component + firstFragmentModule

            activityDependency = injectNow(SOME_DEPENDENCY)
            fragmentDependency = injectNow(FRAGMENT_DEPENDENCY1)

            view?.textView?.text =
                context?.getString(R.string.first_fragment, activityDependency, fragmentDependency)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_first, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fragmentDelegate.onActivityCreated()
    }
}
