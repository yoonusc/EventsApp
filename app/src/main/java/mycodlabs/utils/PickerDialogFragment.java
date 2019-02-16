package mycodlabs.utils;

/**
 * Created by yoonus on 5/22/2018.
 */



        import android.util.Log;
        import android.widget.TextView;

        import org.greenrobot.eventbus.Subscribe;

        import mobi.upod.timedurationpicker.TimeDurationPickerDialogFragment;
        import mobi.upod.timedurationpicker.TimeDurationPicker;
        import mobi.upod.timedurationpicker.TimeDurationUtil;
        import mycodlabs.adapters.Clickcb;
        import mycodlabs.instapp.PrefManager;

        import static android.content.ContentValues.TAG;

public class PickerDialogFragment extends TimeDurationPickerDialogFragment {


    String duration="";
    PrefManager prefManager;
    Clickcb clickcb;
    public  static  PickerDialogFragment pickerDialogFragment;
    @Override
    protected long getInitialDuration() {
        return 0 * 60 * 1000;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);

    }

    public  PickerDialogFragment geInstance(Clickcb clickcb)
    {
        if(pickerDialogFragment==null)
        {
           pickerDialogFragment=new PickerDialogFragment();
            GlobalBus.getBus().register(this);
        }

        return  pickerDialogFragment;
    }

    @Override
    protected int setTimeUnits() {
        return TimeDurationPicker.HH_MM;
    }

    @Override
    public void onDurationSet(TimeDurationPicker view, long duration) {
        prefManager=new PrefManager(getActivity());
        final String formattedDuration = TimeDurationUtil.formatHoursMinutesSeconds(duration);
        this.duration=formattedDuration;
        prefManager.setDuration(formattedDuration);
        Events.FragmentActivityMessage fragmentActivityMessageEvent =
                new Events.FragmentActivityMessage(
                        formattedDuration);
        GlobalBus.getBus().post(fragmentActivityMessageEvent);
    }

    @Subscribe
    public void getMessage(Events.ActivityFragmentMessage activityFragmentMessage) {
         this.duration=activityFragmentMessage.getMessage();
        Log.d(TAG, "getMessage:"+activityFragmentMessage.getMessage());
    }
}