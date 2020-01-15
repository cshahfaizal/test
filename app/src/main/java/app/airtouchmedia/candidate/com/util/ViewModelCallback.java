package app.airtouchmedia.candidate.com.util;

import android.arch.lifecycle.ViewModel;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

/**
 * View model call back class for Binding Observable
 */
public class ViewModelCallback extends ViewModel implements Observable {

    private PropertyChangeRegistry registry = new PropertyChangeRegistry();

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.add(callback);

    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.remove(callback);

    }

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    public void notifyChange() {
        registry.notifyCallbacks(this, 0, null);
    }

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with [Bindable] to generate a field in
     * `BR` to be used as `fieldId`.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    public void notifyPropertyChanged(int fieldId) {
        registry.notifyCallbacks(this, fieldId, null);
    }
}
