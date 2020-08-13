package com.hudson.wanandroid.ui.fix;

/**
 * Created by Hudson on 2020/8/7 0007.
 */

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;

import java.util.ArrayDeque;

/**
 * 自定义的FragmentNavigator，以解决视图状态的保存问题
 *
 * 注意：backStack失效了，原因是使用hide和show方式时，
 * 利用backStack会出现fragment视图重叠问题，可能原因是
 * https://stackoverflow.com/questions/16189088/overlapping-hidden-fragments-after-application-gets-killed-and-restored
 * https://issuetracker.google.com/issues/36992082#makechanges
 * 因此将涉及到backStack的逻辑注释
 *
 *
 * 在官方示例中还有个视图状态解决方案，见NavigationAdvanceSample
 * https://github.com/android/architecture-components-samples/tree/master/NavigationAdvancedSample
 *
 *
 * 注意：
 *  删除掉NavHostFragment标签中的app:navGraph属性，并代码调用setGraph
 *  详见 {@link com.hudson.wanandroid.ui.activity.MainActivity}
 *
 */
@Navigator.Name("wanFragment") //在nav_graph_main的标签中有使用该名
public class WanAndroidNavigator extends Navigator<androidx.navigation.fragment.FragmentNavigator.Destination> {
    private static final String TAG = "WanAndroidNavigator";
    private static final String KEY_BACK_STACK_IDS = "androidx-nav-fragment:navigator:backStackIds";

    private final Context mContext;
    private final FragmentManager mFragmentManager;
    private final int mContainerId;
    private ArrayDeque<Integer> mBackStack = new ArrayDeque<>();

    public WanAndroidNavigator(@NonNull Context context, @NonNull FragmentManager manager,
                               int containerId) {
        mContext = context;
        mFragmentManager = manager;
        mContainerId = containerId;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method must call
     * {@link FragmentTransaction#setPrimaryNavigationFragment(Fragment)}
     * if the pop succeeded so that the newly visible Fragment can be retrieved with
     * {@link FragmentManager#getPrimaryNavigationFragment()}.
     * <p>
     * Note that the default implementation pops the Fragment
     * asynchronously, so the newly visible Fragment from the back stack
     * is not instantly available after this call completes.
     */
    @Override
    public boolean popBackStack() {
        if (mBackStack.isEmpty()) {
            return false;
        }
        if (mFragmentManager.isStateSaved()) {
            Log.i(TAG, "Ignoring popBackStack() call: FragmentManager has already"
                    + " saved its state");
            return false;
        }
        mFragmentManager.popBackStack(
                generateBackStackName(mBackStack.size(), mBackStack.peekLast()),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        mBackStack.removeLast();
        return true;
    }

    @NonNull
    @Override
    public androidx.navigation.fragment.FragmentNavigator.Destination createDestination() {
        return new androidx.navigation.fragment.FragmentNavigator.Destination(this);
    }

    /**
     * Instantiates the Fragment via the FragmentManager's
     * {@link androidx.fragment.app.FragmentFactory}.
     *
     * Note that this method is <strong>not</strong> responsible for calling
     * {@link Fragment#setArguments(Bundle)} on the returned Fragment instance.
     *
     * @param context Context providing the correct {@link ClassLoader}
     * @param fragmentManager FragmentManager the Fragment will be added to
     * @param className The Fragment to instantiate
     * @param args The Fragment's arguments, if any
     * @return A new fragment instance.
     * @deprecated Set a custom {@link androidx.fragment.app.FragmentFactory} via
     * {@link FragmentManager#setFragmentFactory(FragmentFactory)} to control
     * instantiation of Fragments.
     */
    @SuppressWarnings("DeprecatedIsStillUsed") // needed to maintain forward compatibility
    @Deprecated
    @NonNull
    public Fragment instantiateFragment(@NonNull Context context,
                                        @NonNull FragmentManager fragmentManager,
                                        @NonNull String className, @SuppressWarnings("unused") @Nullable Bundle args) {
        return fragmentManager.getFragmentFactory().instantiate(
                context.getClassLoader(), className);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method should always call
     * {@link FragmentTransaction#setPrimaryNavigationFragment(Fragment)}
     * so that the Fragment associated with the new destination can be retrieved with
     * {@link FragmentManager#getPrimaryNavigationFragment()}.
     * <p>
     * Note that the default implementation commits the new Fragment
     * asynchronously, so the new Fragment is not instantly available
     * after this call completes.
     */
    @SuppressWarnings("deprecation") /* Using instantiateFragment for forward compatibility */
    @Nullable
    @Override
    public NavDestination navigate(@NonNull androidx.navigation.fragment.FragmentNavigator.Destination destination, @Nullable Bundle args,
                                   @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {
        if (mFragmentManager.isStateSaved()) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already"
                    + " saved its state");
            return null;
        }
        String className = destination.getClassName();
        if (className.charAt(0) == '.') {
            className = mContext.getPackageName() + className;
        }
        final FragmentTransaction ft = mFragmentManager.beginTransaction();

        int enterAnim = navOptions != null ? navOptions.getEnterAnim() : -1;
        int exitAnim = navOptions != null ? navOptions.getExitAnim() : -1;
        int popEnterAnim = navOptions != null ? navOptions.getPopEnterAnim() : -1;
        int popExitAnim = navOptions != null ? navOptions.getPopExitAnim() : -1;
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = enterAnim != -1 ? enterAnim : 0;
            exitAnim = exitAnim != -1 ? exitAnim : 0;
            popEnterAnim = popEnterAnim != -1 ? popEnterAnim : 0;
            popExitAnim = popExitAnim != -1 ? popExitAnim : 0;
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
        }

        // ============== change start ========================
        // this is google source code, we change this logic
//        ft.replace(mContainerId, frag);
        boolean initialNavigation = false;
        // hide old
        Fragment oldFragment = mFragmentManager.getPrimaryNavigationFragment();
        if(oldFragment != null) {
            ft.hide(oldFragment);
        }else{
            initialNavigation = true;
        }
        // get new fragment
        String tag = String.valueOf(destination.getId());
        Fragment newFragment = mFragmentManager.findFragmentByTag(tag);
        if(newFragment == null){
            newFragment = instantiateFragment(mContext, mFragmentManager,
                    className, args);
            ft.add(mContainerId, newFragment, tag);
        } else {
            // already exist, just show
            ft.show(newFragment);
        }
        // ============== change end ========================

        ft.setPrimaryNavigationFragment(newFragment);

//        final @IdRes int destId = destination.getId();
//        final boolean initialNavigation = mBackStack.isEmpty();
//        final boolean isSingleTopReplacement = navOptions != null && !initialNavigation
//                && navOptions.shouldLaunchSingleTop()
//                && mBackStack.peekLast() == destId;

//        boolean isAdded;
//        if (initialNavigation) {
//            isAdded = true;
//        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
//            if (mBackStack.size() > 1) {
//                // If the Fragment to be replaced is on the FragmentManager's
//                // back stack, a simple replace() isn't enough so we
//                // remove it from the back stack and put our replacement
//                // on the back stack in its place
//                mFragmentManager.popBackStack(
//                        generateBackStackName(mBackStack.size(), mBackStack.peekLast()),
//                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                ft.addToBackStack(generateBackStackName(mBackStack.size(), destId));
//            }
//            isAdded = false;
//        } else {
//            ft.addToBackStack(generateBackStackName(mBackStack.size() + 1, destId));
//            isAdded = true;
//        }
//        if (navigatorExtras instanceof androidx.navigation.fragment.FragmentNavigator.Extras) {
//            androidx.navigation.fragment.FragmentNavigator.Extras extras = (androidx.navigation.fragment.FragmentNavigator.Extras) navigatorExtras;
//            for (Map.Entry<View, String> sharedElement : extras.getSharedElements().entrySet()) {
//                ft.addSharedElement(sharedElement.getKey(), sharedElement.getValue());
//            }
//        }
        ft.setReorderingAllowed(true);
        ft.commit();
        // The commit succeeded, update our view of the world
        if (initialNavigation) {
//            mBackStack.add(destId);
            return destination;
        } else {
            return null;
        }
    }

    @Override
    @Nullable
    public Bundle onSaveState() {
        Bundle b = new Bundle();
        int[] backStack = new int[mBackStack.size()];
        int index = 0;
        for (Integer id : mBackStack) {
            backStack[index++] = id;
        }
        b.putIntArray(KEY_BACK_STACK_IDS, backStack);
        return b;
    }

    @Override
    public void onRestoreState(@Nullable Bundle savedState) {
        if (savedState != null) {
            int[] backStack = savedState.getIntArray(KEY_BACK_STACK_IDS);
            if (backStack != null) {
                mBackStack.clear();
                for (int destId : backStack) {
                    mBackStack.add(destId);
                }
            }
        }
    }

    @NonNull
    private String generateBackStackName(int backStackIndex, int destId) {
        return backStackIndex + "-" + destId;
    }
}
