package org.rewedigital.katana.android.example.fragment.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.android.example.R
import org.rewedigital.katana.android.example.fragment.fragmentActivityModule
import org.rewedigital.katana.android.modules.ActivityModule

/**
 * @see FirstFragment
 * @see SecondFragment
 */
class FragmentActivity : AppCompatActivity(), KatanaTrait {

    override val component: Component = Component(
        modules = listOf(
            ActivityModule(this),
            fragmentActivityModule
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fragment)
    }
}
