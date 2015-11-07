package com.example.Bama.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import com.example.Bama.R;

/**
 * ListView����ˢ�ºͼ��ظ��
 *
 * @change JohnWatson ���ԭ�����޸ĺ����޸����޸ĵ���������ԭ��汾��
 * �����޸ģ���������   ˢ�»���ʱ��� ��Ч
 */
public class RefreshListView extends ListView implements OnScrollListener {
	private final static String TAG = RefreshListView.class.getName();
	/**
	 * ����ģ��
	 */
	private final static String DATE_FORMAT_STR = "MM��dd�� HH:mm";

	/**
	 * ��ָ����Ļ�ϻ���������ʵ��headview�ı��˾������ *
	 */
	private final static int HEADMOVESIZE = 3;

	/**
	 * ���ֻ���״̬
	 */
	private final static int RELEASE_TO_REFRESH = 0; // ��ָ������Ļ�ϣ��������ˢ�µ�״̬
	private final static int PULL_TO_REFRESH = 1; // �����У�׼��ˢ�£����ǻ�û�е�����ˢ�µ�״̬
	private final static int REFRESHING = 2; // ˢ����
	private final static int REFRESHDONE = 3; // ˢ�����

	private final static int LOADING = 4; // �ײ����ظ����

	/**
	 * ������
	 */
	private final static int ENDINT_LOADING = 1;
	/**
	 * �ֶ����ˢ��
	 */
	private final static int ENDINT_MANUAL_LOAD_DONE = 2;
	/**
	 * �Զ����ˢ��
	 */
	private final static int ENDINT_AUTO_LOAD_DONE = 3;

	/**
	 * HeadViewˢ�µ�״̬ *
	 */
	private int headState;
	/**
	 * FootViewˢ�µ�״̬ *
	 */
	private int endState;

	/**
	 * �Ƿ���Լ��ظ�� *
	 */
	private boolean canLoadMore = true;
	/**
	 * �Ƿ��������ˢ�� *
	 */
	private boolean canRefresh = false;
	/**
	 * �Ƿ�����Զ����ظ�ࣨҪ���ж��Ƿ��м��ظ�࣬���û�У����flagҲû�����壩
	 */
	private boolean isAutoLoadMore = true;
	/**
	 * ����ˢ����ɺ��Ƿ���ʾ��һ��Item
	 */
	private boolean isMoveToFirstItemAfterRefresh = false;

	/**
	 * -------------------------------�����ķָ���----------------------------------- *
	 */

	private LayoutInflater mInflater;

	private LinearLayout headView; // ����ˢ��view
	private TextView tipsTextView; // ˢ����ʾ��λ��
	//	private TextView lastUpdatedTextView; // ���ˢ��ʱ������
	private ImageView arrowImageView; // ˢ�µļ�ͷ
	private ProgressBar progressBar; // ˢ��progressBar

	private View endRootView; // �ײ�����View
	private ProgressBar endLoadProgressBar; // �ײ������е�progressBar
	private TextView endLoadTipsTextView; // �ײ�������ʾ������

	/**
	 * headView����
	 */
	private RotateAnimation arrowAnim;
	/**
	 * headView��ת����
	 */
	private RotateAnimation arrowReverseAnim;

	/**
	 * ���ڱ�֤startY��ֵ��һ�������touch�¼���ֻ����¼һ��
	 */
	private boolean isRecored;

	private int headViewWidth;
	private int headViewHeight;

	/**
	 * ��ʼY���λ�� *
	 */
	private int startY;
	/**
	 * �Ƿ���RELEASE_To_REFRESH״̬ת������ *
	 */
	private boolean isBack;

	private int firstItemIndex;
	private int lastItemIndex;
	private int count;
	private boolean enoughCount;// �Ƿ��㹻����������Ļ

	private OnRefreshListener refreshListener;
	private OnLoadMoreListener loadMoreListener;

	public RefreshListView(Context pContext, AttributeSet pAttrs) {
		super(pContext, pAttrs);
		init(pContext);
	}

	public RefreshListView(Context pContext) {
		super(pContext);
		init(pContext);
	}

	public RefreshListView(Context pContext, AttributeSet pAttrs, int pDefStyle) {
		super(pContext, pAttrs, pDefStyle);
		init(pContext);
	}

	public boolean isCanLoadMore() {
		return canLoadMore;
	}

	public void setCanLoadMore(boolean pCanLoadMore) {
		canLoadMore = pCanLoadMore;
		if (canLoadMore && getFooterViewsCount() == 0) {
			addFooterView();
		}
	}

	public boolean isCanRefresh() {
		return canRefresh;
	}

	public void setCanRefresh(boolean pCanRefresh) {
		canRefresh = pCanRefresh;
	}

	public boolean isAutoLoadMore() {
		return isAutoLoadMore;
	}

	public void setAutoLoadMore(boolean pIsAutoLoadMore) {
		isAutoLoadMore = pIsAutoLoadMore;
	}

	public boolean isMoveToFirstItemAfterRefresh() {
		return isMoveToFirstItemAfterRefresh;
	}

	public void setMoveToFirstItemAfterRefresh(boolean pIsMoveToFirstItemAfterRefresh) {
		isMoveToFirstItemAfterRefresh = pIsMoveToFirstItemAfterRefresh;
	}

	/**
	 * ��ʼ������
	 */
	private void init(Context pContext) {
		setCacheColorHint(Color.parseColor("#00000000"));
		mInflater = LayoutInflater.from(pContext);
		addHeadView();
		setOnScrollListener(this);
		initHeadArrowAnimation(0);
	}

	/**
	 * �������ˢ�µ�HeadView
	 */
	private void addHeadView() {
		headView = (LinearLayout) mInflater.inflate(R.layout.refresh_listview_head, null);
		arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
		tipsTextView = (TextView) headView.findViewById(R.id.head_tipsTextView);
		//		lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);
		measureView(headView);
		headViewHeight = headView.getMeasuredHeight();
		headViewWidth = headView.getMeasuredWidth();
		headView.setPadding(0, -1 * headViewHeight, 0, 0);
		headView.invalidate();
		addHeaderView(headView, null, false);
		headState = REFRESHDONE;
	}

	/**
	 * ��Ӽ��ظ��FootView
	 */
	private void addFooterView() {
		endRootView = mInflater.inflate(R.layout.refresh_listview_footer, null);
		endRootView.setVisibility(View.VISIBLE);
		endLoadProgressBar = (ProgressBar) endRootView.findViewById(R.id.pull_to_refresh_progress);
		endLoadTipsTextView = (TextView) endRootView.findViewById(R.id.load_more);
		endRootView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (canLoadMore) {
					if (canRefresh) {
						// ����������ˢ��ʱ�����FootViewû�����ڼ��أ�����HeadViewû������ˢ�£��ſ��Ե�����ظ�ࡣ
						if (endState != ENDINT_LOADING && headState != REFRESHING) {
							endState = ENDINT_LOADING;
							onLoadMore();
						}
					} else if (endState != ENDINT_LOADING) {
						// ����������ˢ��ʱ��FootView�����ڼ���ʱ���ſ��Ե�����ظ�ࡣ
						endState = ENDINT_LOADING;
						onLoadMore();
					}
				}
			}
		});
		addFooterView(endRootView);
		if (isAutoLoadMore) {
			endState = ENDINT_AUTO_LOAD_DONE;
		} else {
			endState = ENDINT_MANUAL_LOAD_DONE;
		}
	}

	/**
	 * ��ʼ������ˢ�µļ�ͷ�Ķ���Ч��
	 */
	private void initHeadArrowAnimation(final int pAnimDuration) {

		int _Duration;

		if (pAnimDuration > 0) {
			_Duration = pAnimDuration;
		} else {
			_Duration = 250;
		}
		Interpolator _Interpolator = new LinearInterpolator();
		arrowAnim = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		arrowAnim.setInterpolator(_Interpolator);
		arrowAnim.setDuration(_Duration);
		arrowAnim.setFillAfter(true);
		arrowReverseAnim = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		arrowReverseAnim.setInterpolator(_Interpolator);
		arrowReverseAnim.setDuration(_Duration);
		arrowReverseAnim.setFillAfter(true);
	}

	/**
	 * ����HeadView���
	 */
	private void measureView(View pChild) {
		ViewGroup.LayoutParams p = pChild.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;

		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		pChild.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * ����ˢ�¼���ӿ�
	 */
	public interface OnRefreshListener {
		public void onRefresh();
	}

	/**
	 * ���ظ�����ӿ�
	 */
	public interface OnLoadMoreListener {
		public void onLoadMore();
	}

	public void setOnRefreshListener(OnRefreshListener pRefreshListener) {
		if (pRefreshListener != null) {
			refreshListener = pRefreshListener;
			canRefresh = true;
		}
	}

	public void setOnLoadListener(OnLoadMoreListener pLoadMoreListener) {
		if (pLoadMoreListener != null) {
			loadMoreListener = pLoadMoreListener;
			//�Լ�ע�ӵ�
			//			canLoadMore = true;
			if (canLoadMore && getFooterViewsCount() == 0) {
				addFooterView();
			}
		}
	}

	public OnLoadMoreListener getLoadListener() {
		return loadMoreListener;
	}

	/**
	 * ��������ˢ��
	 */
	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	/**
	 * ����ˢ�����
	 */
	public void onRefreshComplete() {
		// ����ˢ�º��Ƿ���ʾ��һ��Item
		if (isMoveToFirstItemAfterRefresh)
			setSelection(getTop());
		headState = REFRESHDONE;
		// ������: Time
		//		lastUpdatedTextView.setText(getResources().getString(R.string.p2refresh_refresh_lasttime)
		//				+ new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
		changeHeaderViewByState();
	}

	/**
	 * ���ڼ��ظ�࣬FootView��ʾ �� ������...
	 */
	private void onLoadMore() {
		if (loadMoreListener != null) {
			// ������...
			endLoadTipsTextView.setText(R.string.p2refresh_doing_end_refresh);
			endLoadTipsTextView.setVisibility(View.VISIBLE);
			endLoadProgressBar.setVisibility(View.VISIBLE);

			loadMoreListener.onLoadMore();
		}
	}

	/**
	 * ���ظ�����
	 */
	public void onLoadMoreComplete() {
		if (isAutoLoadMore) {
			endState = ENDINT_AUTO_LOAD_DONE;
		} else {
			endState = ENDINT_MANUAL_LOAD_DONE;
		}
		changeEndViewByState();
	}

	/**
	 * ����ˢ��ʱ��
	 */
	public void setAdapter(BaseAdapter adapter) {
		// ������: Time
		//		lastUpdatedTextView.setText(getResources().getString(R.string.p2refresh_refresh_lasttime)
		//				+ new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
		super.setAdapter(adapter);
	}

	/**
	 * �жϻ�����ListView�ײ�û
	 */
	@Override
	public void onScroll(AbsListView pView, int pFirstVisibleItem, int pVisibleItemCount, int pTotalItemCount) {
		firstItemIndex = pFirstVisibleItem;
		lastItemIndex = pFirstVisibleItem + pVisibleItemCount - 2;
		count = pTotalItemCount - 2;
		if (pTotalItemCount > pVisibleItemCount) {
			enoughCount = true;
			// endingView.setVisibility(View.VISIBLE);
		} else {
			enoughCount = false;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView pView, int pScrollState) {
		if (canLoadMore) {// ���ڼ��ظ�๦��
			if (lastItemIndex == count && pScrollState == SCROLL_STATE_IDLE) {
				// SCROLL_STATE_IDLE=0������ֹͣ
				if (endState != ENDINT_LOADING) {
					if (isAutoLoadMore) {// �Զ����ظ�࣬������FootView��ʾ ���� �ࡱ
						if (canRefresh) {
							// ��������ˢ�²���HeadViewû������ˢ��ʱ��FootView�����Զ����ظ�ࡣ
							if (headState != REFRESHING) {
								// FootView��ʾ : �� �� ---> ������...
								endState = ENDINT_LOADING;
								onLoadMore();
								changeEndViewByState();
							}
						} else {// û������ˢ�£�����ֱ�ӽ��м��ظ�ࡣ
							// FootView��ʾ : �� �� ---> ������...
							endState = ENDINT_LOADING;
							onLoadMore();
							changeEndViewByState();
						}
					} else {// �����Զ����ظ�࣬������FootView��ʾ ��������ء�
						// FootView��ʾ : ������� ---> ������...
						endState = ENDINT_MANUAL_LOAD_DONE;
						changeEndViewByState();
					}
				}
			}
		} else if (endRootView != null && endRootView.getVisibility() == VISIBLE) {
			// ͻȻ�رռ��ظ�๦��֮������Ҫ�Ƴ�FootView��
			Log.w(TAG, "this.removeFooterView(endRootView);...");
			endRootView.setVisibility(View.GONE);
			this.removeFooterView(endRootView);
		}
	}

	/**
	 * �ı���ظ��״̬
	 */
	private void changeEndViewByState() {
		if (canLoadMore) {
			// ������ظ��
			switch (endState) {
			case ENDINT_LOADING:// ˢ����

				// ������...
				if (endLoadTipsTextView.getText().equals(R.string.p2refresh_doing_end_refresh)) {
					break;
				}
				endLoadTipsTextView.setText(R.string.p2refresh_doing_end_refresh);
				endLoadTipsTextView.setVisibility(View.VISIBLE);
				endLoadProgressBar.setVisibility(View.VISIBLE);
				break;
			case ENDINT_MANUAL_LOAD_DONE:// �ֶ�ˢ�����

				// �������
				endLoadTipsTextView.setText(R.string.p2refresh_end_click_load_more);
				endLoadTipsTextView.setVisibility(View.VISIBLE);
				endLoadProgressBar.setVisibility(View.GONE);

				endRootView.setVisibility(View.VISIBLE);
				break;
			case ENDINT_AUTO_LOAD_DONE:// �Զ�ˢ�����
				// �� ��
				endLoadTipsTextView.setText(R.string.p2refresh_end_load_more);
				endLoadTipsTextView.setVisibility(View.VISIBLE);
				endLoadProgressBar.setVisibility(View.GONE);
				endRootView.setVisibility(View.VISIBLE);
				break;
			default:
				// ԭ���Ĵ�����Ϊ�ˣ� ������item�ĸ߶�С��ListView����ĸ߶�ʱ��
				// Ҫ���ص�FootView������Լ�ȥԭ���ߵĴ���ο���

				if (enoughCount) {
					endRootView.setVisibility(View.VISIBLE);
				} else {
					endRootView.setVisibility(View.GONE);
				}
				break;
			}
		} else {
			endRootView.setVisibility(View.GONE);
			this.removeFooterView(endRootView);
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (canRefresh) {
			if (canLoadMore && endState == ENDINT_LOADING) {
				// �����ڼ��ظ�๦�ܣ����ҵ�ǰ���ڼ��ظ�࣬Ĭ�ϲ���������ˢ�£����������Ϻ����ʹ�á�
				return super.onTouchEvent(event);
			}

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;

			case MotionEvent.ACTION_UP:

				if (headState != REFRESHING && headState != LOADING) {
					if (headState == REFRESHDONE) {

					}
					if (headState == PULL_TO_REFRESH) {
						headState = REFRESHDONE;
						changeHeaderViewByState();
					}
					if (headState == RELEASE_TO_REFRESH) {
						headState = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}

				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();

				// Log.v("111", mHeadState+"");
				if (!isRecored && firstItemIndex == 0) {
					Log.v("111", "come in");
					isRecored = true;
					startY = tempY;
				}

				if (headState != REFRESHING && isRecored && headState != LOADING) {

					// ��������б?����Ļ�Ļ����������Ƶ�ʱ���б��ͬʱ���й���
					// ��������ȥˢ����
					if (headState == RELEASE_TO_REFRESH) {
						setSelection(0);
						if (((tempY - startY) / HEADMOVESIZE < headViewHeight) && (tempY - startY) > 0) {
							Log.v("111", "RELEASE_TO_REFRESH����ˢ��״̬");
							headState = PULL_TO_REFRESH;
							changeHeaderViewByState();
						} else if (tempY - startY <= 0) {
							Log.v("111", "RELEASE_TO_REFRESH->tempY - mStartY<= 0");
							headState = REFRESHDONE;
							changeHeaderViewByState();
						}
						// �������ˣ����߻�û�����Ƶ���Ļ�����ڸ�head�ĵز�
					}
					if (headState == PULL_TO_REFRESH) {
						setSelection(0);
						// ���������Խ���RELEASE_TO_REFRESH��״̬
						if ((tempY - startY) / HEADMOVESIZE >= headViewHeight) {
							Log.v("111", "PULL_TO_REFRESH����ˢ��״̬");
							headState = RELEASE_TO_REFRESH;
							isBack = true;
							changeHeaderViewByState();
						} else if (tempY - startY <= 0) {
							Log.v("111", "PULL_TO_REFRESH->tempY - mStartY <= 0");
							headState = REFRESHDONE;
							changeHeaderViewByState();
						}
					}

					if (headState == REFRESHDONE) {
						if (tempY - startY > 0) {
							headState = PULL_TO_REFRESH;
							changeHeaderViewByState();
						}
					}

					if (headState == PULL_TO_REFRESH) {
						headView.setPadding(0, -1 * headViewHeight + (tempY - startY) / HEADMOVESIZE, 0, 0);

					}

					if (headState == RELEASE_TO_REFRESH) {
						headView.setPadding(0, (tempY - startY) / HEADMOVESIZE - headViewHeight, 0, 0);
					}
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * HeadView״̬�ı�ʱ�����½���
	 */
	private void changeHeaderViewByState() {
		switch (headState) {
		case RELEASE_TO_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextView.setVisibility(View.VISIBLE);
			//			lastUpdatedTextView.setVisibility(View.VISIBLE);
			//TODO time
			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(arrowAnim);
			// �ɿ�ˢ��
			tipsTextView.setText(R.string.p2refresh_release_refresh);

			break;
		case PULL_TO_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextView.setVisibility(View.VISIBLE);
			//			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(arrowReverseAnim);
				// ����ˢ��
				tipsTextView.setText(R.string.p2refresh_pull_to_refresh);
			} else {
				// ����ˢ��
				tipsTextView.setText(R.string.p2refresh_pull_to_refresh);
			}
			break;

		case REFRESHING:
			headView.setPadding(0, 0, 0, 0);

			// ����Ľ��飺
			// ʵ���������setPadding�����ö��������档

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			// ����ˢ��...
			tipsTextView.setText(R.string.p2refresh_doing_head_refresh);
			//			lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		case REFRESHDONE:
			headView.setPadding(0, -1 * headViewHeight, 0, 0);
			// �˴����ԸĽ�ͬ��������
			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.arrow);
			// ����ˢ��
			tipsTextView.setText(R.string.p2refresh_pull_to_refresh);
			//			lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		}
	}

}
