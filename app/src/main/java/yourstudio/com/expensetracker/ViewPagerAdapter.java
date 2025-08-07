package yourstudio.com.expensetracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;



public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new IncomeFragment();
            case 1:
                return new ExpenseFragment();
            case 2:
                return new BudgetsFragment();
            case 3:
                return new CategoriesFragment();
            default:
                return new IncomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Total number of tabs
    }
}
