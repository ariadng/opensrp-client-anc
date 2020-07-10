package org.smartregister.anc.library.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.anc.library.AncLibrary;
import org.smartregister.anc.library.R;
import org.smartregister.anc.library.activity.PopulationCharacteristicsActivity;
import org.smartregister.anc.library.activity.SiteCharacteristicsActivity;
import org.smartregister.anc.library.presenter.MePresenter;
import org.smartregister.anc.library.util.Utils;
import org.smartregister.util.LangUtils;
import org.smartregister.view.activity.DrishtiApplication;
import org.smartregister.view.contract.MeContract;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

public class MeFragment extends org.smartregister.view.fragment.MeFragment implements MeContract.View {
    private RelativeLayout mePopCharacteristicsSection;
    private RelativeLayout siteCharacteristicsSection;
    private RelativeLayout languageSwitcherSection;
    private TextView languageSwitcherText;
    private Map<String, Locale> locales = new HashMap<>();
    private String[] languages;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    protected void setUpViews(View view) {
        super.setUpViews(view);
        mePopCharacteristicsSection = view.findViewById(R.id.me_pop_characteristics_section);
        siteCharacteristicsSection = view.findViewById(R.id.site_characteristics_section);

        if (Utils.enableLanguageSwitching()) {
            languageSwitcherSection = view.findViewById(R.id.language_switcher_section);
            languageSwitcherSection.setVisibility(View.VISIBLE);

            View meLanguageSwitcherSeparator = view.findViewById(R.id.me_language_switcher_separator);
            meLanguageSwitcherSeparator.setVisibility(View.VISIBLE);

            languageSwitcherText = languageSwitcherSection.findViewById(R.id.language_switcher_text);
            registerLanguageSwitcher();
        }
    }

    protected void setClickListeners() {
        super.setClickListeners();
        mePopCharacteristicsSection.setOnClickListener(meFragmentActionHandler);
        siteCharacteristicsSection.setOnClickListener(meFragmentActionHandler);
        if (Utils.enableLanguageSwitching()) {
            languageSwitcherSection.setOnClickListener(meFragmentActionHandler);
        }
    }

    protected void initializePresenter() {
        presenter = new MePresenter(this);
    }

    @Override
    protected void onViewClicked(View view) {
        int viewId = view.getId();
        if (viewId == R.id.logout_section) {
            DrishtiApplication.getInstance().logoutCurrentUser();
        } else if (viewId == R.id.site_characteristics_section) {
            if (getContext() != null) {
                getContext().startActivity(new Intent(getContext(), SiteCharacteristicsActivity.class));
            }
        } else if (viewId == R.id.me_pop_characteristics_section) {
            if (getContext() != null) {
                getContext().startActivity(new Intent(getContext(), PopulationCharacteristicsActivity.class));
            }
        } else if (viewId == R.id.language_switcher_section) {
            languageSwitcherDialog();
        }
    }

    private void languageSwitcherDialog() {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getActivity().getResources().getString(R.string.choose_language));
            builder.setItems(languages, (dialog, position) -> {
                if (MeFragment.this.getActivity() != null) {
                    String selectedLanguage = languages[position];
                    languageSwitcherText.setText(String.format(MeFragment.this.getActivity().getResources().getString(R.string.default_language_string), selectedLanguage));

                    saveLanguage(selectedLanguage);
                    MeFragment.this.reloadClass();
                    AncLibrary.getInstance().notifyAppContextChange();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void saveLanguage(String selectedLanguage) {
        if (MeFragment.this.getActivity() != null && StringUtils.isNotBlank(selectedLanguage)) {
            Locale selectedLanguageLocale = locales.get(selectedLanguage);
            if (selectedLanguageLocale != null) {
                LangUtils.saveLanguage(MeFragment.this.getActivity().getApplication(), selectedLanguageLocale.getLanguage());
            } else {
                Timber.i("Language could not be set");
            }
        }
    }

    private void reloadClass() {
        if (getActivity() != null) {
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            getActivity().startActivity(intent);
        }
    }

    private void registerLanguageSwitcher() {
        if (getActivity() != null) {
            addLanguages();

            languages = new String[locales.size()];
            Locale current = getActivity().getResources().getConfiguration().locale;
            int x = 0;
            for (Map.Entry<String, Locale> language : locales.entrySet()) {
                languages[x] = language.getKey(); //Update the languages strings array with the languages to be displayed on the alert dialog
                x++;

                if (current.getLanguage().equals(language.getValue().getLanguage())) {
                    languageSwitcherText.setText(String.format(getActivity().getResources().getString(R.string.default_language_string), language.getKey()));
                }
            }
        }
    }

    private void addLanguages() {
        locales.put(getString(R.string.english_language), Locale.ENGLISH);
        locales.put("French", Locale.FRENCH);
    }

}