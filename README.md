# SlideRuleView(仿薄荷健康的滑动尺效果)
## 最终实现的效果
![](https://raw.githubusercontent.com/houtrry/SlideRuleView/master/img/gif2.gif)
## 分析
这个滑动尺的绘制部分并没什么难点, 有问题的地方是滑动的细节.  
1. 手指离开后, 继续滑动  
2. 滑动停止时, 如果中间的绿色刻度线不在滑动尺的刻度线位置, 则移动到最近的刻度线位置  
## 实现思路
1. 将控件拆分成可滑动部分(RulerScaleView)和不可滑动部分(TickMarkView).    
		* 不可滑动部分只有中间的那个绿色刻度线(在这里, 我把它称作标准刻度线)  
		* 可滑动部分包括除了标准线之外的其他全部  
2. 自定义ViewGroup(在代码中是SlideRuleView), 将RulerScaleView和TickMarkView作为子组件.  
3. 滑动逻辑使用ViewDragHelper来处理.  
		* ViewDragHelper.Callback.tryCaptureView: RulerScaleView响应滑动, TickMarkView不响应.  
		* ViewDragHelper.Callback.clampViewPositionHorizontal: 控制滑动边界  
		* ViewDragHelper.Callback.onViewReleased: 手指松开时, 通过ViewDragHelper.flingCapturedView方法, 实现卷尺的fling效果. flingCapturedView方法的四个参数, 控制fling的边界.  
		* ViewDragHelper.Callback.onViewPositionChanged: 监听的滑动过程中, 这个方法一直有调用, 所以, 在这里处理监听  
		* ViewDragHelper.Callback.onViewDragStateChanged: Drag的状态发生改变会回调这个方法. 所以, 如果Drag的状态由其他状态变为空闲状态时, 判断一下当前的中间位置是否是刻度线位置, 如果不是, 则滑动到最近的刻度线位置(使用ViewDragHelper.smoothSlideViewTo方法).  
		
## 注意事项
1. ViewDragHelper的使用.   
		* 不要忘记重写onInterceptTouchEvent, onTouchEvent和computeScroll方法.  
		* 滑动过程中边界控制  
		* fling过程中的边界控制  
