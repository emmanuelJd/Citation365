package com.citation.emmanuel.citation365.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.citation.emmanuel.citation365.Auteur;
import com.citation.emmanuel.citation365.R;
import com.citation.emmanuel.citation365.models.CitationItem;
import com.citation.emmanuel.citation365.models.CitationViewObject;

import java.util.List;

/**
 * Created by emmanuel on 05/10/2015.
 *
 * l'adapter pour la liste des citations
 */
public class CitationListAdapterRecherche extends ArrayAdapter<CitationItem> implements View.OnClickListener/*, View.OnTouchListener*/{

    private Context context;
    private int resLayout;
    private List<CitationItem> listCitationItems;
    private ListView lvCitation;


    /** constructeur de l'adapter  */
    public CitationListAdapterRecherche(Context context, int resLayout, List<CitationItem> listCitationItems) {
        super(context, resLayout, listCitationItems);

        this.context = context;
        this.resLayout = resLayout;
        this.listCitationItems = listCitationItems;
    }

    /** constructeur de l'adapter  */
    public CitationListAdapterRecherche(Context context, int resLayout, List<CitationItem> listCitationItems, ListView lvCitation) {
        super(context, resLayout, listCitationItems);

        this.context = context;
        this.resLayout = resLayout;
        this.listCitationItems = listCitationItems;
        this.lvCitation = lvCitation;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = null;

        if (convertView == null) {
            v = layoutInflater.inflate(R.layout.item_citation_list, null);
        } else {
            v = convertView;
        }

        /** on récupère les éléments qui contiendront la citation et l'auteur*/
        TextView tvCitation = (TextView) v.findViewById(R.id.citation_text);
        TextView tvAuteur = (TextView) v.findViewById(R.id.citation_auteur);

        /** on récupère la variable contenant les informations */
        CitationItem citationItem = listCitationItems.get(position);

        /** on insert les informations dans les éléments*/
        tvCitation.setText(citationItem.getCitation());
        tvAuteur.setText(citationItem.getAuteur());


        /** on récupère les boutons pour appliquer l'image associé et ajouter les listeners */
        ImageButton imBfavoris = (ImageButton) v.findViewById(R.id.favoris_bouton);
        ImageButton imVAuteur = (ImageButton) v.findViewById(R.id.voir_auteur);
        ImageButton imCCitation = (ImageButton) v.findViewById(R.id.copy_citation);
        ImageButton imShare = (ImageButton) v.findViewById(R.id.share_bouton);

        imVAuteur.setBackgroundResource(R.drawable.icon_citation);
        imCCitation.setBackgroundResource(R.drawable.icon_citation);
        imShare.setBackgroundResource(R.drawable.icon_citation);
        imBfavoris.setBackgroundResource(R.drawable.icon_citation);

        if(citationItem.getFavoris() == 1) {
            imBfavoris.setImageResource(R.drawable.star_fill);
        }else{
            imBfavoris.setImageResource(R.drawable.star_empty);
        }
        imBfavoris.setTag(new CitationViewObject(citationItem, null));

        imVAuteur.setTag(citationItem.getId_auteur_link());

        imCCitation.setTag(citationItem.getCitation());

        imShare.setTag(citationItem.getCitation() + " - " +citationItem.getAuteur());

        imVAuteur.setOnClickListener(this);
        imBfavoris.setOnClickListener(this);
        imCCitation.setOnClickListener(this);
        imShare.setOnClickListener(this);

        v.setOnTouchListener(new SwipeDetector(this));
        v.setTag(new CitationViewObject(citationItem, null));

        return v;
    }

    /** listener d'un clique sur la liste des citations*/
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.favoris_bouton :  /** récupération de la citation enregistrer dans l'item  et enregistrement en favoris dans la base de données */
                                        CitationItem citationItemTag = (CitationItem) ((CitationViewObject) v.getTag()).getCitation();
                                        CitationListAdapterFavoris.insertData(citationItemTag, v, this, getContext(), "accueil");
                                        break;

            case R.id.voir_auteur :     String url_auteur = (String) v.getTag();
                                        CitationListAdapterRecherche.information_auteur(getContext(), url_auteur);
                                        break;

            case R.id.copy_citation :   String citation_copy = (String) v.getTag();
                                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("citation365", citation_copy);
                                        clipboard.setPrimaryClip(clip);
                                        Toast.makeText(getContext(), "Copie effectuée !", Toast.LENGTH_SHORT).show();
                                        break;

            case R.id.share_bouton :    String citation_share = (String) v.getTag();
                                        Intent share = new Intent(Intent.ACTION_SEND);
                                        share.setType("text/plain");
                                        share.putExtra(Intent.EXTRA_TEXT, citation_share);

                                        getContext().startActivity(Intent.createChooser(share, "Partager cette citation !"));
                                        break;

            default :   break;
        }
    }


    /**
     * fonction pour accéder aux informations de l'auteur
     */
    public static void information_auteur(Context context, String url_auteur){

        Intent intent = new Intent(context, Auteur.class);

        intent.putExtra("URL_AUTEUR", "http://evene.lefigaro.fr" + url_auteur);

        context.startActivity(intent);

        ((Activity)context).overridePendingTransition(R.anim.slide_enter_2, R.anim.slide_leave_2);

    }

    /**
     * la classe swipeDetector est l'implémentation d'un listener pour l'évèneme de swipe sur les citations, sur la page d'accueil le swip de la gauche vers la droite permet la mise en favoris de la citation
     */
    public class SwipeDetector implements View.OnTouchListener {

        private static final int MIN_DISTANCE_X = 300;
        private static final int MIN_DISTANCE_Y = 50;

        private static final int MIN_LOCK_DISTANCE_X = 30;
        private static final int MIN_LOCK_DISTANCE_Y = 50;

        private boolean motionInterceptDisallowed = false;
        private float downX, upX, downY, upY;

        private ArrayAdapter parentAdapter = null;

        public SwipeDetector(ArrayAdapter arrayAdapter){
            super();
            this.parentAdapter = arrayAdapter;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {

                /** début du swap initialisation de la position en x et y */
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    downY = event.getRawY();
                    //Log.e(" ACTION down ", ""+downX);
                    return true;
                }

                /** lors du déplacement on vérifie la direction, si elle correspond à un début de swipe vers la droite on bloque supprime la propagation de l'évènement pour éviter un cancel du au scroll
                 * une fois grace au flag motionInterceptDisallowed */
                case MotionEvent.ACTION_MOVE: {

                    upX = event.getX();
                    upY = event.getRawY();

                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    if ( (deltaX) < MIN_LOCK_DISTANCE_X && Math.abs(deltaY) < MIN_LOCK_DISTANCE_Y && !motionInterceptDisallowed) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        motionInterceptDisallowed = true;
                    }
                    //Log.e(" ACTION move ", ""+deltaX);
                    return true;
                }

                /** Lors de la fin de l'action du potentielle swipe on vérifie les coordonnées de la fin de l'action, on détermine si le swipe est valide et si il l'est on ajoute au favoris */
                case MotionEvent.ACTION_UP: {

                    upX = event.getX();
                    upY = event.getRawY();

                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    if ((deltaX) >= MIN_DISTANCE_X && Math.abs(deltaY) <= MIN_DISTANCE_Y ) {

                        CitationItem citationItemTag = (CitationItem) ((CitationViewObject) v.getTag()).getCitation();
                        ImageButton imageButtonFav = (ImageButton) v.findViewById(R.id.favoris_bouton);

                        swipe(v, context);
                        CitationListAdapterFavoris.insertData(citationItemTag, imageButtonFav, this.parentAdapter, getContext(), "accueil");
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                    }else{
                    }

                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    //Log.e(" ACTION up ", "" + deltaX);
                    return true;
                }
                /** si l'action a été annulé pour une raison on vérifie la trajectoire du swipe  et si elle correspond on réalise l'ajout comme un succes*/
                case MotionEvent.ACTION_CANCEL: {

                    upX = event.getX();
                    upY = event.getRawY();
                    float deltaX = downX - upX;
                    float deltaY = downY - upY ;

                    if ((deltaX) >= MIN_DISTANCE_X/3 &&  Math.abs(deltaY) <= MIN_DISTANCE_Y ) {

                        CitationItem citationItemTag = (CitationItem) ((CitationViewObject) v.getTag()).getCitation();
                        ImageButton imageButtonFav = (ImageButton) v.findViewById(R.id.favoris_bouton);
                        swipe(v, context);
                        CitationListAdapterFavoris.insertData(citationItemTag, imageButtonFav, this.parentAdapter, getContext(), "accueil");
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                    } else {
                    }
                    //Log.e(" ACTION cancel ", ""+deltaX);
                    return false;
                }
            }

            return true;
        }

        /**  fonction qui permet de lancer l'animation de l'item citation du swipe de gauche vers droite */
        private void swipe(View view, Context context) {

            final View animView = view;

            final Animation animation_out = AnimationUtils.loadAnimation(context,
                    R.anim.slide_x_out_recherche);

            final Animation animation_in = AnimationUtils.loadAnimation(context,
                    R.anim.slide_x_in_recherche);


            animation_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {


                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animView.startAnimation(animation_in);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animView.startAnimation(animation_out);

        }

    }

}
