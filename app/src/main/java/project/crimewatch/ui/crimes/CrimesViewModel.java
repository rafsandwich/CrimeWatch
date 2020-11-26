package project.crimewatch.ui.crimes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CrimesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CrimesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is crimes fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}