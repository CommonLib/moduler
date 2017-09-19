package com.architecture.extend.baselib.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.architecture.extend.baselib.R;
import com.architecture.extend.baselib.mvvm.BaseFragment;

import java.util.LinkedList;

/**
 *
 */
public final class FragmentStack {
    private LinkedList<BaseFragment> mStack = new LinkedList<>();
    private FragmentManager mFragmentManager;
    private int mContainerId;
    private TopFragmentChangeListener mTopFragmentChangeListener;

    private FragmentStack(FragmentManager fragmentManager, int containerId) {
        mFragmentManager = fragmentManager;
        this.mContainerId = containerId;
    }

    /**
     * Create an instance for a specific container.
     */
    public static FragmentStack create(FragmentManager fragmentManager, int containerId) {
        return new FragmentStack(fragmentManager, containerId);
    }

    public int size() {
        return mStack.size();
    }

    public Fragment peek() {
        return mStack.peekLast();
    }

    /**
     * Replaces the entire mStack with this fragment.
     */
    public void replace(BaseFragment fragment, String tag) {
        if (fragment == null) {
            return;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (fragment.isAdded()) {
            clear(transaction, fragment);
            showFragment(transaction, fragment, tag);
        } else {
            clear(transaction);
            push(transaction, fragment, tag);
        }
        transaction.commit();
    }

    public LinkedList<BaseFragment> getLinkedList() {
        return mStack;
    }

    /**
     * Adds a new fragment to the mStack and displays it.
     */
    public void push(BaseFragment fragment, String tag) {
        if (fragment == null) {
            return;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if(getLinkedList().size() >= 1){
            transaction.setCustomAnimations(R.anim.activity_right_in, R.anim.activity_left_out, 0, 0);
        }
        showFragment(transaction, fragment, tag);
        transaction.commit();
    }

    public Fragment pop() {
        if (mStack.size() > 1) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction
                    .setCustomAnimations(R.anim.activity_left_in,R.anim.activity_right_out, 0, 0);
            Fragment popFragment = mStack.pollLast();
            removeFragment(transaction, popFragment);
            BaseFragment f = mStack.peekLast();
            showFragment(transaction, f, f.getTag());
            transaction.commit();
            return popFragment;
        }
        return null;
    }

    public boolean hasBackStack() {
        return mStack.size() > 1;
    }

    public void removeFragment(Fragment fragment) {
        if (fragment != null && fragment.isAdded()) {
            mFragmentManager.beginTransaction().remove(fragment).commit();
            if (mStack.contains(fragment)) {
                mStack.remove(fragment);
            }
        }
    }

    private void detachTop(FragmentTransaction transaction) {
        if (mStack.size() > 0) {
            Fragment f = mStack.peekLast();
            if (f != null && f.isAdded() && !f.isHidden()) {
                hideFragment(transaction, f);
            }
        }
    }

    private void showFragment(FragmentTransaction transaction, BaseFragment fragment, String tag) {
        if (fragment == null) {
            return;
        }
        if (fragment.isAdded()) {
            if (fragment.isHidden()) {
                detachTop(transaction);
                transaction.show(fragment);
                mStack.remove(fragment);
                mStack.addLast(fragment);
                if (mTopFragmentChangeListener != null) {
                    mTopFragmentChangeListener.onTopFragmentChange(fragment);
                }
            }
            // fragment is in show state, do nothing.
        } else {
            //fragment is not added, add and show fragment.
            push(transaction, fragment, tag);
        }
    }

    /**
     * hide fragment divide hide() and remove()
     *
     * @param transaction
     * @param fragment
     */
    private void hideFragment(FragmentTransaction transaction, Fragment fragment) {
        transaction.hide(fragment);
    }

    public void clear() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        clear(transaction);
        transaction.commit();
    }

    private void clear(FragmentTransaction transaction) {
        while (mStack.size() > 0) {
            Fragment fragment = mStack.pollLast();
            removeFragment(transaction, fragment);
        }
    }

    /**
     * clear stack but except one fragment which is already in stack.
     *
     * @param transaction
     * @param exceptFragment fragment is in stack
     */
    private void clear(FragmentTransaction transaction, BaseFragment exceptFragment) {
        while (mStack.size() > 0) {
            Fragment fragment = mStack.pollLast();
            if (fragment != exceptFragment) {
                removeFragment(transaction, fragment);
            }
        }
        mStack.addLast(exceptFragment);
    }

    private void removeFragment(FragmentTransaction transaction, Fragment fragment) {
        if (fragment == null) {
            return;
        }
        if (fragment.isAdded()) {
            transaction.remove(fragment);
        }
    }

    public void setTopFragmentChangeListener(TopFragmentChangeListener topFragmentChangeListener) {
        mTopFragmentChangeListener = topFragmentChangeListener;
    }

    public interface TopFragmentChangeListener {
        void onTopFragmentChange(Fragment topFragment);
    }

    private void push(FragmentTransaction transaction, BaseFragment fragment, String tag) {
        if (fragment != null && !fragment.isAdded()) {
            detachTop(transaction);
            transaction.add(mContainerId, fragment, tag);
            if (mTopFragmentChangeListener != null) {
                mTopFragmentChangeListener.onTopFragmentChange(fragment);
            }
            mStack.addLast(fragment);
        }
    }

    public BaseFragment getTopShowFragment() {
        return mStack.peekLast();
    }
}
