package org.rewedigital.katana.android.example.fragment.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_second.view.*
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.android.example.R
import org.rewedigital.katana.android.example.fragment.inject.Container
import org.rewedigital.katana.android.example.fragment.secondFragmentModule
import org.rewedigital.katana.injectNow

/**
 * See [FirstFragment] first.
 * This is an alternative approach to the Fragment/Activity relation issue.
 *
 * @see FirstFragment
 * @see secondFragmentModule
 */
class SecondFragment : Fragment(),
                       KatanaTrait {

    override lateinit var component: Component
    private lateinit var container: Container

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_second, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        component = (activity as KatanaTrait).component + secondFragmentModule

        container = injectNow()

        view?.textView?.text = context?.getString(
            R.string.second_fragment,
            container.activityDependency,
            container.fragmentDependency
        )
    }
}
