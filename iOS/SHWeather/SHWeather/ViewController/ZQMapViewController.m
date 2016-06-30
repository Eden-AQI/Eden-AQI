//
//  ZQMapViewController.m
//  SHWeather
//
//  Created by xiao on 5/14/16.
//  Copyright © 2016 xiao. All rights reserved.
//
#define ScreenWidth 320

#import "ZQMapViewController.h"
#import "MyBMKPointAnnotation.h"

@interface ZQMapViewController ()<BMKMapViewDelegate>

@property (nonatomic) BOOL viewWillFirstAppear;
@property (weak, nonatomic) IBOutlet UIView *mapContentView;

@property (strong, nonatomic) NSDictionary *siteInfoDic;

@end

@implementation ZQMapViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.mapView = [[BMKMapView alloc]initWithFrame:self.mapContentView.bounds];
    BMKCoordinateRegion region ;//表示范围的结构体
    region.center = CLLocationCoordinate2DMake(34.753251, 113.633361);//中心点
    region.span.latitudeDelta = 0.3;//经度范围（设置为0.1表示显示范围为0.2的纬度范围）
    region.span.longitudeDelta = 0.3;//纬度范围
    [self.mapView setRegion:region animated:YES];
    [self.mapContentView addSubview:self.mapView];
    // Do any additional setup after loading the view from its nib.
}

- (void)viewWillAppear:(BOOL)animated
{
    AppDelegate *app = (AppDelegate *)[UIApplication sharedApplication].delegate;
    app.revealSideViewController.panInteractionsWhenClosed =PPRevealSideInteractionNone;
    
    [self.mapView viewWillAppear];
    self.mapView.delegate = self;
    
    [self getCitiesData];
//    if (self.viewWillFirstAppear == NO) {
//        [self getCitiesData];
//    }
//    self.viewWillFirstAppear = YES;
}

- (void)getCitiesData
{
//    if ([DefaultsHandler sharedInstance].siteListInfo) {
//        //update map
//        self.siteInfoDic = [DefaultsHandler sharedInstance].siteListInfo;
//        [self updateMap];
//        return;
//    }
    NSString *api = [Global api:[NSString stringWithFormat:@"Aqi/GetCityList"]];
    AFHTTPRequestOperationManager *request = [Global createRequest];
    [request GET:api parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSArray *responseArray = (NSArray *)responseObject;
        NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithCapacity:10];
        NSMutableDictionary *citiesNameDic = [NSMutableDictionary dictionaryWithCapacity:10];
        for (int i = 0; i<responseArray.count; i++) {
            NSDictionary *itemDic = [responseArray objectAtIndex:i];
            NSArray *cityDataArray = [itemDic objectForKey:@"Items"];
            [dic setObject:cityDataArray forKey:[itemDic objectForKey:@"GroupName"]];
            NSMutableArray *muArray = [NSMutableArray arrayWithCapacity:10];
            for (int j = 0; j<cityDataArray.count; j++) {
                NSDictionary *cityDic = [cityDataArray objectAtIndex:j];
                [muArray addObject:[cityDic objectForKey:@"Name"]];
            }
            [citiesNameDic setObject:muArray forKey:[itemDic objectForKey:@"GroupName"]];
        }
        [DefaultsHandler sharedInstance].siteListInfo = dic;
        [DefaultsHandler sharedInstance].siteListDic = citiesNameDic;
        self.siteInfoDic = dic;
        [self updateMap];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [[[UIAlertView alloc] initWithTitle:@"提示" message:@"数据加载失败" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles: nil] show];
    }];

}

- (void)updateMap
{
    [self.mapView removeOverlays:_mapView.overlays];
    [self addPointAnnotation];
}

//添加标注
- (void)addPointAnnotation
{
    NSArray *dataArray = self.siteInfoDic.allValues;
    for (NSArray *itemArray in dataArray) {
        for (int i=0; i<itemArray.count; i++) {
            NSDictionary *siteDic = [itemArray objectAtIndex:i];
            BMKPointAnnotation *pointAnnotation = [[BMKPointAnnotation alloc]init];
            CLLocationCoordinate2D coor;
            coor.latitude = [[siteDic objectForKey:@"Latitude"] floatValue];
            coor.longitude = [[siteDic objectForKey:@"Longitude"] floatValue];
            pointAnnotation.coordinate = coor;
            pointAnnotation.title = [NSString stringWithFormat:@"%@",[siteDic objectForKey:@"Aqi"]];
            pointAnnotation.subtitle = [NSString stringWithFormat:@"%@",[siteDic objectForKey:@"Grade"]];
            [_mapView addAnnotation:pointAnnotation];
            
        }
    }
}

#pragma mark -
#pragma mark implement BMKMapViewDelegate

/**
 * 当选中一个annotation views时，调用此接口
 * @param mapView 地图View
 * @param views 选中的annotation views
 */
- (void)mapView:(BMKMapView *)mapView didSelectAnnotationView:(BMKAnnotationView *)view
{
    NSLog(@"didSelectAnnotationView");
//    _shopCoor = view.annotation.coordinate;
}

// 根据anntation生成对应的View
- (BMKAnnotationView *)mapView:(BMKMapView *)mapView viewForAnnotation:(id <BMKAnnotation>)annotation
{
    static NSString *identifier = @"MKAnnotationView";
    BMKAnnotationView *pin = [mapView dequeueReusableAnnotationViewWithIdentifier:identifier];
    
    if (pin == nil) {
        pin = [[BMKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:identifier];
        //在图中我们可以看到图标的上方，有个气泡弹窗里面写着当前用户的位置所在地
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 43, 25)];
        label.layer.cornerRadius = 2.0;
        label.clipsToBounds = YES;
        label.backgroundColor = [DefaultsHandler getColorOrderLevel:annotation.subtitle];
        label.text = annotation.title;
        label.textAlignment = NSTextAlignmentCenter;
//        BMKActionPaopaoView *paopaoView = [[BMKActionPaopaoView alloc]initWithCustomView:label];
//        pin.paopaoView = paopaoView;
        [pin addSubview:label];
    }
    pin.annotation = annotation;
    pin.paopaoView = [[BMKActionPaopaoView alloc]initWithCustomView:[[UIView alloc] init]];
    
    return pin;
}

// 当点击annotation view弹出的泡泡时，调用此接口
- (void)mapView:(BMKMapView *)mapView annotationViewForBubble:(BMKAnnotationView *)view;
{
    NSLog(@"paopaoclick");
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    self.mapView.frame = self.mapContentView.bounds;
}

- (void)viewWillDisappear:(BOOL)animated
{
    [self.mapView viewWillDisappear];
    self.mapView.delegate = nil;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
