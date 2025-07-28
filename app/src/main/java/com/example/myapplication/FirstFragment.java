package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentFirstBinding;
import com.justbaat.ads.sdk.AdSdkManager;

import java.util.Objects;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        AdSdkManager.INSTANCE.loadRewarded(requireActivity(), "rewarded_placement");
        AdSdkManager.INSTANCE.registerSdkReadyCallback(() -> {
            if (activity != null) {

                ViewGroup bannerContainer = requireView().findViewById(R.id.banner_container_first);
                if (bannerContainer != null) {
                    AdSdkManager.INSTANCE.showBanner(activity, bannerContainer);
                } else {
                    Log.e("FirstFragment", "Banner container is null!");
                }
            } else {
                Log.e("FirstFragment", "Activity is null!");
            }
            return null;
        });

        AdSdkManager.INSTANCE.registerSdkReadyCallback(() -> {
            if (activity != null) {

                ViewGroup nativeAdContainer = requireView().findViewById(R.id.native_ad_container);

                AdSdkManager.INSTANCE.showNativeAd(
                        activity,
                        nativeAdContainer,
                        null,
                        () -> {
                            Log.d("FirstFragment", "Native ad loaded");
                            return null;
                        },
                        error -> {
                            Log.e("FirstFragment", "Native ad failed to load: " + error);
                            return null;
                        }
                );
            }
            return null;
        });




        binding.buttonFirst.setOnClickListener(v -> {
            if (activity != null) {
                binding.buttonFirst.setEnabled(false);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    binding.buttonFirst.setEnabled(true);
                }, 5000);
                AdSdkManager.INSTANCE.showRewardedAd(
                        activity,
                        "rewarded_placement",
                        true,
                        1,
                        reward -> {
                            Log.d("FirstFragment", "User earned reward: " + reward.toString());
                            NavHostFragment.findNavController(FirstFragment.this)
                                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
                            return null;
                        },
                        () -> {
                            Log.d("FirstFragment", "Ad was dismissed");
                            NavHostFragment.findNavController(FirstFragment.this)
                                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
                            return null;
                        },
                        error -> {
                            Log.e("FirstFragment", "Ad failed to show: " + error);
                            NavHostFragment.findNavController(FirstFragment.this)
                                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
                            return null;
                        }
                );
            }

        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}