package org.rmj.guanzongroup.marketplace.Fragment;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.marketplace.Activity.Activity_PlaceOrder;
import org.rmj.guanzongroup.marketplace.Activity.Activity_ProductOverview;
import org.rmj.guanzongroup.marketplace.Adapter.Adapter_ItemCart;
import org.rmj.guanzongroup.marketplace.Adapter.Adapter_ProductList;
import org.rmj.guanzongroup.marketplace.Etc.OnTransactionsCallback;
import org.rmj.guanzongroup.marketplace.Model.ItemCartModel;
import org.rmj.guanzongroup.marketplace.R;
import org.rmj.guanzongroup.marketplace.ViewModel.VMMPItemCart;

import java.text.DecimalFormat;
import java.util.List;

public class Fragment_MPItemCart extends Fragment {

    private VMMPItemCart mViewModel;

    private RecyclerView recyclerView, rvSuggest;
    private LinearLayout noItem, lnMPFooter;
    private Dialog_Loading poLoading;
    private MessageBox poDialogx;
    private MaterialButton btnCheckOut;
    private ImageButton btnDelete;
    private TextView lblGrandTotal, lblTotalCount;
    private CheckBox cbSelectAll;
    private CardView cvSelectAll;
    private Adapter_ItemCart adapter;

    private int nTotCount = 0;
    private int nSelected = 0;

    public static Fragment_MPItemCart newInstance() {
        return new Fragment_MPItemCart();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mp_item_cart, container, false);
        v.setVisibility(View.GONE);

        initWidgets(v);

        mViewModel = new ViewModelProvider(requireActivity()).get(VMMPItemCart.class);
        poDialogx = new MessageBox(requireActivity());
        poDialogx.initDialog();

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));

        mViewModel.GetCartItemsList().observe(getViewLifecycleOwner(), items -> {
            try {

                List<ItemCartModel> itemCart = mViewModel.ParseDataForAdapter(items);

                if (itemCart.size() > 0){
                    v.setVisibility(View.VISIBLE);
                    cvSelectAll.setVisibility(View.VISIBLE);
                    noItem.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    lnMPFooter.setVisibility(View.VISIBLE);
                    adapter = new Adapter_ItemCart(itemCart, new Adapter_ItemCart.OnCartAction() {
                        @Override
                        public void onItemSelect(String fsListIdx) {
                            mViewModel.forCheckOut(fsListIdx);
                        }

                        @Override
                        public void onItemDeselect(String fsListIdx) {
                            mViewModel.removeForCheckOut(fsListIdx);
                        }

                        @Override
                        public void onItemDelete(String fsListIDx) {
                            mViewModel.RemoveItemOnCart(fsListIDx, new OnTransactionsCallback() {
                                @Override
                                public void onLoading() {
                                    poLoading.initDialog("Item Cart", "Processing. Please wait.");
                                    poLoading.show();
                                }

                                @Override
                                public void onSuccess(String fsMessage) {
                                    poLoading.dismiss();
                                }

                                @Override
                                public void onFailed(String fsMessage) {
                                    poLoading.dismiss();

                                    poDialogx.setIcon(R.drawable.baseline_error_24);
                                    poDialogx.setTitle("Item Cart");
                                    poDialogx.setMessage(fsMessage);
                                    poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                                        @Override
                                        public void OnButtonClick(View view, AlertDialog dialog) {
                                            poDialogx.dismiss();
                                        }
                                    });

                                    poDialogx.show();

                                }
                            });
                        }

                        @Override
                        public void onQuantityClick(String fsListIdx, int fnItemQty) {
                            mViewModel.addUpdateCart(fsListIdx, fnItemQty, true,new OnTransactionsCallback() {
                                @Override
                                public void onLoading() {
                                    poLoading.initDialog("Item Cart", "Processing. Please wait.");
                                    poLoading.show();
                                }

                                @Override
                                public void onSuccess(String fsMessage) {
                                    poLoading.dismiss();
                                }

                                @Override
                                public void onFailed(String fsMessage) {
                                    poLoading.dismiss();

                                    poDialogx.setIcon(R.drawable.baseline_error_24);
                                    poDialogx.setTitle("Item Cart");
                                    poDialogx.setMessage(fsMessage);
                                    poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                                        @Override
                                        public void OnButtonClick(View view, AlertDialog dialog) {
                                            poDialogx.dismiss();
                                        }
                                    });

                                    poDialogx.show();

                                }
                            });
                        }
                    });
                    Log.e("itemCart = ", String.valueOf(itemCart.size()));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    v.setVisibility(View.VISIBLE);
                    noItem.setVisibility(View.VISIBLE);
                    cvSelectAll.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    lnMPFooter.setVisibility(View.GONE);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        mViewModel.GetSelectedItemTotalPrice().observe(getViewLifecycleOwner(), subtotal -> {
            try{
                lblGrandTotal.setText("₱ " + currencyFormat(subtotal));
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        mViewModel.GetMpItemCartCount().observe(getViewLifecycleOwner(), count -> {
            try{
                if(count > 0){
                    if(count == 1){
                        cbSelectAll.setText("Select All(1 Item)");
                    } else {
                        cbSelectAll.setText("Select All(" + count+ " Items)");
                    }
                }
                nTotCount = count;
                setupSelectAllCheckBox();
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        mViewModel.GetSelectedItemCartTotalCount().observe(getViewLifecycleOwner(), count -> {
            try{
                if(count == 0){
                    lblTotalCount.setText("Total(" + count + ")item");
                    btnDelete.setVisibility(View.GONE);
                } else if(count == 1){
                    lblTotalCount.setText("Total(" + count + ")item");
                    btnDelete.setVisibility(View.VISIBLE);
                } else {
                    lblTotalCount.setText("Total(" + count + ")items");
                    btnDelete.setVisibility(View.VISIBLE);
                }
                nSelected = count;
                setupSelectAllCheckBox();
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        // Show items below cart
        showSuggestions();

        cbSelectAll.setOnClickListener(v13 -> {
            boolean isChecked = cbSelectAll.isChecked();
            mViewModel.SelectAllItemOnCart(isChecked);
            if(isChecked) {
                btnDelete.setVisibility(View.VISIBLE);
            } else {
                btnDelete.setVisibility(View.GONE);
            }
        });

        btnDelete.setOnClickListener(v12 -> {
            poMessage.setButtonText("Yes", "No");
            poMessage.initDialog("Marketplace Item Cart", "Remove selected items?", new Dialog_DoubleButton.OnDialogConfirmation() {
                @Override
                public void onConfirm(AlertDialog dialog) {
                    dialog.dismiss();
                    DeleteAllItemOnCart();
                }

                @Override
                public void onCancel(AlertDialog dialog) {
                    dialog.dismiss();
                }
            });
            poMessage.show();
        });

        btnCheckOut.setOnClickListener(view ->{
            mViewModel.checkCartItemsForCheckOut(new OnTransactionsCallback() {
                @Override
                public void onLoading() {
                    poLoading.initDialog("Item Cart", "Processing. Please wait.");
                    poLoading.show();
                }

                @Override
                public void onSuccess(String fsMessage) {
                    poLoading.dismiss();
                    Intent loIntent = new Intent(requireActivity(), Activity_PlaceOrder.class);
                    loIntent.putExtra("cBuyNowxx", false);
                    startActivity(loIntent);
                }

                @Override
                public void onFailed(String fsMessage) {
                    poLoading.dismiss();

                    poDialogx.setIcon(R.drawable.baseline_error_24);
                    poDialogx.setTitle("Item Cart");
                    poDialogx.setMessage(fsMessage);
                    poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            poDialogx.dismiss();
                        }
                    });

                    poDialogx.show();

                }
            });
        });
        return v;
    }

    private void initWidgets(View view){
        poLoading = new Dialog_Loading(requireActivity());
        recyclerView = view.findViewById(R.id.recyclerView_MPCart);
        rvSuggest = view.findViewById(R.id.rvSuggest);
        noItem = view.findViewById(R.id.layoutMPNoItem);
        lnMPFooter = view.findViewById(R.id.lnMPFooter);
        lblGrandTotal = view.findViewById(R.id.lblMPGrandTotal);
        lblTotalCount = view.findViewById(R.id.lblMpTotalItems);
        btnCheckOut = view.findViewById(R.id.btnMPCheckOut);
        cbSelectAll = view.findViewById(R.id.cbSelectAll);
        btnDelete = view.findViewById(R.id.btnRemoveAll);
        cvSelectAll = view.findViewById(R.id.cvSelectAll);
    }

    public static String currencyFormat(double amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(amount);
    }

    private void showSuggestions() {
        rvSuggest.setLayoutManager(new GridLayoutManager(requireActivity(),
                2, RecyclerView.VERTICAL, false));
        rvSuggest.setHasFixedSize(true);
        mViewModel.getProductList(0).observe(getViewLifecycleOwner(), products -> {
            try {
                if(products.size() > 0) {
                    Adapter_ProductList poAdapter = new Adapter_ProductList(products, listingId -> {
                        Intent loIntent = new Intent(requireActivity(), Activity_ProductOverview.class);
                        loIntent.putExtra("sListngId", listingId);
                        startActivity(loIntent);
                    });
                    poAdapter.notifyDataSetChanged();
                    rvSuggest.setAdapter(poAdapter);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
    }

    private void setupSelectAllCheckBox(){
        if(nTotCount == nSelected){
            cbSelectAll.setChecked(true);
        } else {
            cbSelectAll.setChecked(false);
        }
    }

    private void DeleteAllItemOnCart(){
        mViewModel.DeleteAllSelected(new OnTransactionsCallback() {
            @Override
            public void onLoading() {
                poLoading.initDialog("Item Cart", "Removing items. Please wait.");
                poLoading.show();
            }

            @Override
            public void onSuccess(String fsMessage) {
                poLoading.dismiss();
            }

            @Override
            public void onFailed(String fsMessage) {
                poLoading.dismiss();

                poDialogx.setIcon(R.drawable.baseline_error_24);
                poDialogx.setTitle("Item Cart");
                poDialogx.setMessage(fsMessage);
                poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                    @Override
                    public void OnButtonClick(View view, AlertDialog dialog) {
                        poDialogx.dismiss();
                    }
                });

                poDialogx.show();

            }
        });
    }
}