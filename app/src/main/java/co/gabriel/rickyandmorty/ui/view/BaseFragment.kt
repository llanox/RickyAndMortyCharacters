package co.gabriel.rickyandmorty.ui.view

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

open class BaseFragment : Fragment() {

    fun showError(message: String?) {
        if (message != null) {
            Snackbar.make(this.requireView(), message, Snackbar.LENGTH_LONG).show()
        }
    }

    fun showLoading() {
        Snackbar.make(this.requireView(), "Loading..", Snackbar.LENGTH_LONG).show()
    }

}