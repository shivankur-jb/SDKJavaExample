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

import com.example.myapplication.databinding.FragmentSecondBinding;
import com.justbaat.ads.sdk.AdSdkManager;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            ViewGroup bannerContainer = requireView().findViewById(R.id.banner_container_second);
            if (bannerContainer != null) {
                AdSdkManager.INSTANCE.showBanner(activity, bannerContainer);
            } else {
                Log.e("Second Fragment", "Banner container is null!");
            }
        } else {
            Log.e("SecondFragment", "Activity is null!");
        }


        binding.buttonSecond.setOnClickListener(v -> {
            if (activity != null) {
                binding.buttonSecond.setEnabled(false);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    binding.buttonSecond.setEnabled(true);
                }, 5000);
                AdSdkManager.INSTANCE.showInterstitialAd(
                        activity,
                        "interstitial_placement",
                        false,
                        0,
                        () -> {
                            NavHostFragment.findNavController(SecondFragment.this)
                                    .navigate(R.id.action_SecondFragment_to_FirstFragment);
                            return null;
                        },
                        (error) -> {
                            Log.e("SecondFragment", "Interstitial ad failed: " + error);

                            NavHostFragment.findNavController(SecondFragment.this)
                                    .navigate(R.id.action_SecondFragment_to_FirstFragment);;
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