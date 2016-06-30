//
//  ZQWeatherViewController.m
//  SHWeather
//
//  Created by xiao on 5/14/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import "ZQWeatherViewController.h"
#import "AQICollectionViewCell.h"
#import "DetailCollectionViewCell.h"
#import "HoursChartCollectionViewCell.h"
#import "AQIRecordCollectionViewCell.h"
#import "MJRefresh.h"
#import "Global.h"
#import "WeatherViewModel.h"
#import "ZQAQIDetailViewController.h"
#import "NSString+Util.h"
#import "SiteModel.h"

#import <ShareSDK/ShareSDK.h>
#import <ShareSDKUI/ShareSDK+SSUI.h>

@interface ZQWeatherViewController ()<AQICollectionViewCellDelegate,UIScrollViewDelegate>
@property (weak, nonatomic) IBOutlet UIImageView *bgImageView;

@property (weak, nonatomic) IBOutlet UIView *headerView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *addItem;
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *shareBtn;
@property (weak, nonatomic) IBOutlet UICollectionView *customCollectionView;
@property (strong, nonatomic) UICollectionViewFlowLayout *flowlayout;
@property (strong, nonatomic) NSDictionary *weatherData;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollContentView;
@property (weak, nonatomic) IBOutlet UIPageControl *pageControl;
@property (strong, nonatomic) UIVisualEffectView *effectview;

@property (strong, nonatomic) NSArray *cellArray;
@property (strong, nonatomic) NSMutableArray *citiesArray;
@property (strong, nonatomic) NSString *currentSiteId;
@property (nonatomic) NSInteger pageSign;
@property (nonatomic) BOOL viewWillFirstAppear;
@end

@implementation ZQWeatherViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.citiesArray = [[NSMutableArray alloc]initWithArray:[DefaultsHandler searchDataForKey:FOLLOWCITIES]];
    
    [self loadRealTimeData];
    
    self.cellArray = @[[AQICollectionViewCell new],[DetailCollectionViewCell new],[HoursChartCollectionViewCell new],[AQIRecordCollectionViewCell new]];
    
    self.flowlayout=[[UICollectionViewFlowLayout alloc]init];
    [self.flowlayout setItemSize:CGSizeMake(self.view.bounds.size.width,200)];
    [self.flowlayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    [self.flowlayout setSectionInset:UIEdgeInsetsMake(0, 0, 0, 0)];
    
    //index = 0
    [self initialCollectionView:self.customCollectionView];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(changeCurrentSiteWithNoti:) name:CHANGESITE_NOTI object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(insertNewCityWithNoti:) name:INSERTSITE_NOTI object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(deleteCityWithNoti:) name:DELETESITE_NOTI object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(getGradeInfoUpdateUI) name:GETGRADEINFO_NOTI object:nil];
    
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    AppDelegate *app = (AppDelegate *)[UIApplication sharedApplication].delegate;
    app.revealSideViewController.tapInteractionsWhenOpened = PPRevealSideInteractionNone;
    //    NSLog(@"ZQWeatherViewController viewWillAppear");
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    if (self.viewWillFirstAppear == NO) {
        UIBlurEffect *blur = [UIBlurEffect effectWithStyle:UIBlurEffectStyleLight];
        
        self.effectview = [[UIVisualEffectView alloc] initWithEffect:blur];
        self.effectview.alpha = 0.2;
        self.effectview.frame = self.bgImageView.bounds;
        
        [self.bgImageView addSubview:self.effectview];
        [self initialScrollContentView];
    }
    self.viewWillFirstAppear = YES;
}

//
- (void)initialScrollContentView
{
    self.pageControl.numberOfPages = self.citiesArray.count;
    
    for (UICollectionView *view in self.scrollContentView.subviews) {
        UICollectionView *collectionView = (UICollectionView *)view;
        NSLog(@"----pageCount:%ld",(long)self.pageControl.currentPage);
        if (collectionView.tag != 10) {
            [collectionView removeFromSuperview];
        }
    }
    
    if (self.citiesArray.count>1) {
        self.pageControl.hidden = NO;
        for (int i=1; i<self.citiesArray.count; i++) {
            UICollectionView *otherCollectionView = [[UICollectionView alloc]initWithFrame:CGRectMake(i*self.scrollContentView.frame.size.width, 0,self.scrollContentView.frame.size.width, self.scrollContentView.frame.size.height) collectionViewLayout:self.flowlayout];
            otherCollectionView.tag = 10+i;
            otherCollectionView.backgroundColor = [UIColor clearColor];
            otherCollectionView.delegate = self;
            otherCollectionView.dataSource = self;
            [self initialCollectionView:otherCollectionView];
            [self.scrollContentView addSubview:otherCollectionView];
        }
    }else{
        self.pageControl.hidden = YES;
        self.pageControl.currentPage = 0;
    }
    self.scrollContentView.contentSize = CGSizeMake(self.scrollContentView.frame.size.width*self.citiesArray.count, self.scrollContentView.frame.size.height);
    self.scrollContentView.pagingEnabled = YES;
    [self loadRealTimeData];
    //[[self getCurrentSelectedCollectionView].mj_header beginRefreshing];

}

- (void)initialCollectionView:(UICollectionView *)collectionView
{
    collectionView.collectionViewLayout = self.flowlayout;
    [collectionView registerClass:[AQICollectionViewCell class] forCellWithReuseIdentifier:@"AQICollectionViewCell"];
    [collectionView registerClass:[DetailCollectionViewCell class] forCellWithReuseIdentifier:@"DetailCollectionViewCell"];
    [collectionView registerClass:[HoursChartCollectionViewCell class] forCellWithReuseIdentifier:@"HoursChartCollectionViewCell"];
    [collectionView registerClass:[AQIRecordCollectionViewCell class] forCellWithReuseIdentifier:@"AQIRecordCollectionViewCell"];
    [collectionView registerClass:[UICollectionViewCell class] forCellWithReuseIdentifier:@"EmptyCollectionViewCell"];
    
    // 设置header
    MJRefreshNormalHeader *collectionViewHeader = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(loadRealTimeData)];
    
    // 设置自动切换透明度(在导航栏下面自动隐藏)
    collectionViewHeader.automaticallyChangeAlpha = YES;
    collectionViewHeader.stateLabel.textColor = [UIColor whiteColor];
    //    header.arrowView.alpha = 0;
    collectionViewHeader.activityIndicatorViewStyle = UIActivityIndicatorViewStyleWhite;
    // 隐藏时间
    collectionViewHeader.lastUpdatedTimeLabel.hidden = YES;
    collectionView.mj_header = collectionViewHeader;
}

- (void)loadRealTimeData
{
    NSLog(@"----currentPage:%ld ---pageCount:%ld",self.pageControl.currentPage,self.pageControl.numberOfPages);
    SiteModel *currentSite = (SiteModel *)[self.citiesArray objectAtIndex:self.pageControl.currentPage];
    self.currentSiteId = currentSite.Id;
    NSLog(@"----- current site Id:%@",currentSite.Id);
    NSString *api = [Global api:[NSString stringWithFormat:@"Aqi/GetRealtime?siteId=%@",currentSite.Id]];
    AFHTTPRequestOperationManager *request = [Global createRequest];
    [request GET:api parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        self.weatherData = responseObject;
        NSString *siteName = [self.weatherData objectForKey:@"Name"];
        if (siteName == nil || [siteName isEqual:[NSNull null]]) {
            siteName = @"";
        }
        self.titleLabel.text = siteName;
        
        [self reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [[[UIAlertView alloc] initWithTitle:@"提示" message:@"数据加载失败" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles: nil] show];
        self.weatherData = nil;
        [self reloadData];
    }];

}

- (void)reloadData
{
    UICollectionView *collection = (UICollectionView *)[self getCurrentSelectedCollectionView];
    [collection reloadData];
    if (collection.mj_header.isRefreshing) {
        [collection.mj_header endRefreshing];
    }
//    self.pageControl.currentPage = 2;
//    [self.customCollectionView reloadData];
//    [self.customCollectionView.mj_header endRefreshing];
}

#pragma -mark UICollectionViewDelegate,UICollectionViewDataSource
-(CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout referenceSizeForHeaderInSection:(NSInteger)section
{
    CGSize size=CGSizeMake(SCREEN_WIDTH, 10);
    return size;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    CGSize size = CGSizeZero;
    if (indexPath.row == 0) {
        size = CGSizeMake(SCREEN_WIDTH, SCREEN_HEIGHT - self.headerView.bounds.size.height - self.tabBarController.tabBar.bounds.size.height);
    }else if(indexPath.row<self.cellArray.count){
        UICollectionViewCell *collectionCell = (UICollectionViewCell *)[self.cellArray objectAtIndex:indexPath.row];
        size = CGSizeMake(SCREEN_WIDTH, collectionCell.frame.size.height);
    }else{
        size = CGSizeMake(SCREEN_WIDTH, 120);
    }
    return size;
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.cellArray.count + 1;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    switch (indexPath.row) {
        case 0:
        {
            AQICollectionViewCell *cell = (AQICollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:@"AQICollectionViewCell" forIndexPath:indexPath];
            if (!cell) {
                cell = [self.cellArray objectAtIndex:indexPath.row];
            }
            cell.delegate = self;
            return cell;
        }
            break;
        case 1:
        {
            DetailCollectionViewCell *cell = (DetailCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:@"DetailCollectionViewCell" forIndexPath:indexPath];
            if (!cell) {
                cell = [self.cellArray objectAtIndex:indexPath.row];
            }
            return cell;
        }
            break;
        case 2:
        {
            HoursChartCollectionViewCell *cell = (HoursChartCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:@"HoursChartCollectionViewCell" forIndexPath:indexPath];
            if (!cell) {
                cell = [self.cellArray objectAtIndex:indexPath.row];
            }
            return cell;
        }
            break;
        case 3:
        {
            AQIRecordCollectionViewCell *cell = (AQIRecordCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:@"AQIRecordCollectionViewCell" forIndexPath:indexPath];
            if (!cell) {
                cell = [self.cellArray objectAtIndex:indexPath.row];
            }
            return cell;
        }
            break;
        case 4:
        {
            UICollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"EmptyCollectionViewCell" forIndexPath:indexPath];
            return cell;
        }
        default:
            break;
    }
    return nil;
}

- (void)collectionView:(UICollectionView *)collectionView willDisplayCell:(UICollectionViewCell *)cell forItemAtIndexPath:(NSIndexPath *)indexPath
{
    switch (indexPath.row) {
        case 0:
        {
            AQICollectionViewCell *aqiCell = (AQICollectionViewCell *)cell;
            [aqiCell uploadDataWithModel:[WeatherViewModel returnAQIDataFromData:self.weatherData]];
        }
            break;
        case 1:
        {
            DetailCollectionViewCell *detailCell = (DetailCollectionViewCell *)cell;
            [detailCell uploadDataWithModel:[WeatherViewModel returnDetailDataFromData:self.weatherData]];
        }
            break;
        case 2:
        {
            HoursChartCollectionViewCell *hoursCell = (HoursChartCollectionViewCell *)cell;
            [hoursCell uploadDataWithModel:[WeatherViewModel returnHoursDataFromData:self.weatherData]];
        }
            break;
        case 3:
        {
            AQIRecordCollectionViewCell *recordCell = (AQIRecordCollectionViewCell *)cell;
            [recordCell uploadDataWithModel:[WeatherViewModel returnAQIRecordDataFromData:self.weatherData]];
        }
            break;
        default:
            break;
    }
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row == 0) {
        UICollectionViewCell *cell = [collectionView cellForItemAtIndexPath:indexPath];
        [UIView animateWithDuration:0.3 animations:^{
            collectionView.contentOffset = CGPointMake(0,cell.contentView.bounds.size.height+20);
        }];
    }
}

- (IBAction)addBtnAction:(id)sender {
    
    AppDelegate *app = (AppDelegate *)[UIApplication sharedApplication].delegate;
    [app.revealSideViewController pushOldViewControllerOnDirection:PPRevealSideDirectionLeft animated:YES];

    //[self.revealSideViewController unloadViewControllerForSide:PPRevealSideDirectionLeft];
}

- (IBAction)shareBtnAction:(id)sender {
    //1、创建分享参数
    NSArray* imageArray = @[[UIImage imageNamed:@"appicon.png"]];
    //（注意：图片必须要在Xcode左边目录里面，名称必须要传正确，如果要分享网络图片，可以这样传iamge参数 images:@[@"http://mob.com/Assets/images/logo.png?v=20150320"]）
    if (imageArray) {
        
        NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
        NSString *shareString = [NSString stringWithFormat:@"郑州空气质量\n%@ AQI:%@",self.titleLabel.text,[self.weatherData objectForKey:@"Aqi"]];
        NSString *urlString = [NSString stringWithFormat:@"http://aqi.wuhooooo.com/api/Share?stationCode=%@",self.currentSiteId];
        [shareParams SSDKSetupShareParamsByText:shareString
                                         images:nil
                                            url:[NSURL URLWithString:urlString]
                                          title:@"天气分享"
                                           type:SSDKContentTypeAuto];
        //2、分享（可以弹出我们的分享菜单和编辑界面）
        [ShareSDK showShareActionSheet:nil //要显示菜单的视图, iPad版中此参数作为弹出菜单的参照视图，只有传这个才可以弹出我们的分享菜单，可以传分享的按钮对象或者自己创建小的view 对象，iPhone可以传nil不会影响
                                 items:nil
                           shareParams:shareParams
                   onShareStateChanged:^(SSDKResponseState state, SSDKPlatformType platformType, NSDictionary *userData, SSDKContentEntity *contentEntity, NSError *error, BOOL end) {
                       
                       switch (state) {
                           case SSDKResponseStateSuccess:
                           {
                               UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"分享成功"
                                                                                   message:nil
                                                                                  delegate:nil
                                                                         cancelButtonTitle:@"确定"
                                                                         otherButtonTitles:nil];
                               [alertView show];
                               break;
                           }
                           case SSDKResponseStateFail:
                           {
                               UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"分享失败"
                                                                               message:[NSString stringWithFormat:@"%@",error]
                                                                              delegate:nil
                                                                     cancelButtonTitle:@"OK"
                                                                     otherButtonTitles:nil, nil];
                               [alert show];
                               break;
                           }
                           default:
                               break;
                       }
                   }
         ];}
}

#pragma -mark  AQICollectionViewCellDelegate
- (void)detailBtnAction
{
//    ZQAQIDetailViewController *detailViewController = [[ZQAQIDetailViewController alloc]init];
//    [self.tabBarController presentViewController:detailViewController animated:YES completion:nil];
}

#pragma -mark  scrollViewDelegate
- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView
{
    if ([scrollView isKindOfClass:[UICollectionView class]]) {
        self.pageSign = self.pageControl.currentPage;
        return;
    }
    if (scrollView.contentOffset.x>0) {
        AppDelegate *app = (AppDelegate *)[UIApplication sharedApplication].delegate;
        app.revealSideViewController.panInteractionsWhenClosed = PPRevealSideInteractionNone;
    }
    NSLog(@"1111");
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if ([scrollView isKindOfClass:[UICollectionView class]]) {
        self.effectview.alpha = 0.2+(scrollView.contentOffset.y/scrollView.contentSize.height)*0.6;
        return;
    }
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView
{
    if ([scrollView isKindOfClass:[UICollectionView class]]) {
        return;
    }
    self.pageControl.currentPage = scrollView.contentOffset.x/scrollView.frame.size.width;
    SiteModel *siteModel = [self.citiesArray objectAtIndex:self.pageControl.currentPage];
    self.titleLabel.text = siteModel.name;
    NSLog(@"----9999-%ld",(long)self.pageControl.currentPage);
    if(scrollView.contentOffset.x == 0){
        AppDelegate *app = (AppDelegate *)[UIApplication sharedApplication].delegate;
        app.revealSideViewController.panInteractionsWhenClosed = PPRevealSideInteractionContentView;
    }
    if (self.pageSign != self.pageControl.currentPage) {
        [[self getCurrentSelectedCollectionView].mj_header beginRefreshing];
        NSLog(@"2222");
    }
}

- (UICollectionView *)getCurrentSelectedCollectionView
{
    for (UICollectionView *view in self.scrollContentView.subviews) {
        UICollectionView *collectionView = (UICollectionView *)view;
        NSLog(@"---current-pageCount:%ld",(long)self.pageControl.currentPage);
        if (collectionView.tag == 10+self.pageControl.currentPage) {
            return collectionView;
        }
    }
    return nil;
}

#pragma -mark notification handler
- (void)getGradeInfoUpdateUI
{
    [self reloadData];
}

- (void)changeCurrentSiteWithNoti:(NSNotification *)noti
{
    NSLog(@"----0000000----changeCurrentSiteWithNoti");
    SiteModel *site = (SiteModel *)noti.object;
    NSInteger index = 0;
    BOOL exist = NO;
    for (int i=0;i<self.citiesArray.count;i++) {
        SiteModel *model = [self.citiesArray objectAtIndex:i];
        if ([model.Id isEqualToString:site.Id]) {
            exist = YES;
            index = [self.citiesArray indexOfObject:model];
            break;
        }
    }
    if (exist == NO) {
        [self.citiesArray addObject:site];
        index = self.citiesArray.count - 1;
    }
    
    self.pageControl.currentPage = index;
    self.scrollContentView.contentOffset = CGPointMake(self.scrollContentView.frame.size.width*index, 0);
    [[self getCurrentSelectedCollectionView].mj_header beginRefreshing];
    SiteModel *model = [self.citiesArray objectAtIndex:index];
    self.titleLabel.text = model.name;
    [DefaultsHandler editPairWithData:model withKey:DEFAULTSITE];
}

- (void)insertNewCityWithNoti:(NSNotification *)noti
{
    SiteModel *site = (SiteModel *)noti.object;
    [self.citiesArray addObject:site];
    [DefaultsHandler editPairWithData:site withKey:DEFAULTSITE];
    self.pageControl.numberOfPages = self.citiesArray.count;
    self.pageControl.currentPage = self.pageControl.numberOfPages-1;
    [self initialScrollContentView];
    self.scrollContentView.contentOffset = CGPointMake(self.scrollContentView.frame.size.width*self.pageControl.currentPage, 0);
    self.titleLabel.text = site.name;
}

- (void)deleteCityWithNoti:(NSNotification *)noti
{
    SiteModel *site = (SiteModel *)noti.object;
    for (int i=0;i<self.citiesArray.count;i++) {
        SiteModel *model = [self.citiesArray objectAtIndex:i];
        if ([model.Id isEqualToString:site.Id]) {
            [self.citiesArray removeObject:model];
            break;
        }
    }
//    if (self.citiesArray.count == 1) {
//        self.pageControl.numberOfPages = self.citiesArray.count;
//        self.pageControl.currentPage = 0;
//        self.pageControl.hidden = YES;
//        for (UICollectionView *view in self.scrollContentView.subviews) {
//            UICollectionView *collectionView = (UICollectionView *)view;
//            NSLog(@"----pageCount:%ld",(long)self.pageControl.currentPage);
//            if (collectionView.tag != 10) {
//                [collectionView removeFromSuperview];
//            }
//        }
//        self.titleLabel.text = @"郑州市";
//        [self initialCollectionView:self.customCollectionView];
//        self.scrollContentView.contentSize = CGSizeMake(self.scrollContentView.frame.size.width, self.scrollContentView.frame.size.height);
//        self.scrollContentView.contentOffset = CGPointMake(0, 0);
//        return;
//    }
//    if (self.citiesArray.count == 1) {
//        self.pageControl.numberOfPages = self.citiesArray.count;
//        self.pageControl.currentPage = self.pageControl.numberOfPages-1;
//        [self initialScrollContentView];
//        self.scrollContentView.contentOffset = CGPointMake(self.scrollContentView.frame.size.width*self.pageControl.currentPage, 0);
//        return;
//    }
    if (self.citiesArray.count == 1) {
        self.pageControl.numberOfPages = self.citiesArray.count;
        self.pageControl.currentPage = 0;
    }
    [self initialScrollContentView];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [touches anyObject];
    if ([touch.view isKindOfClass:[UIScrollView class]]) {
        NSLog(@"scrollerView");
    }
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter]removeObserver:self];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
