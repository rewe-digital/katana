package org.rewedigital.katana.android.example.fragment.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.android.example.R
import org.rewedigital.katana.android.example.fragment.FragmentActivityModule
import org.rewedigital.katana.android.example.fragment.FragmentFactoryModule
import org.rewedigital.katana.android.modules.ActivityModule
import org.rewedigital.katana.androidx.fragment.KatanaFragmentFactory
import org.rewedigital.katana.inject

/**
 * @see FirstFragment
 * @see SecondFragment
 */
class FragmentActivity : AppCompatActivity(), KatanaTrait {

    override val component: Component = Component(
        modules = listOf(
            ActivityModule(this),
            FragmentFactoryModule,
            FragmentActivityModule
        )
    )

    private val fragmentFactory by inject<KatanaFragmentFactory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Must be set **before** super call for Fragment instantiation after orientation change
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fragment)
    }
}
