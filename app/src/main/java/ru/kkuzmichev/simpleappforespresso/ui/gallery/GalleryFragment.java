package ru.kkuzmichev.simpleappforespresso.ui.gallery;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import java.util.ArrayList;
import ru.kkuzmichev.simpleappforespresso.R;
import ru.kkuzmichev.simpleappforespresso.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {
    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<GalleryItem> itemList = new ArrayList<>();
    private ProgressBar progressBar;
    private CountingIdlingResource idlingResource = new CountingIdlingResource("GalleryLoad");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        progressBar = root.findViewById(R.id.progress_bar);
        recyclerView = root.findViewById(R.id.recycle_view);
        IdlingRegistry.getInstance().register(idlingResource);
        fakeLoadData();
        setLists();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new GalleryAdapter(itemList));
        return root;
    }
    private void setLists() {
        for (int i = 0; i < 10; i++) {
            itemList.add(new GalleryItem("My title", "My description", (i + 1)));
        }
    }
    private void fakeLoadData() {
        idlingResource.increment();
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                idlingResource.decrement();
                IdlingRegistry.getInstance().unregister(idlingResource);
            }
        }, 1500);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        IdlingRegistry.getInstance().unregister(idlingResource);
        binding = null;
    }
}
