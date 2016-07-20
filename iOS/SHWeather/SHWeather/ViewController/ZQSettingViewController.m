//
//  ZQSettingViewController.m
//  SHWeather
//
//  Created by xiao on 5/14/16.
//  Copyright © 2016 xiao. All rights reserved.
//

#import "ZQSettingViewController.h"
#import "VersionView.h"

@interface ZQSettingViewController ()<UITableViewDelegate,UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UITableView *settingTableView;

@property (strong, nonatomic) NSArray *settingArray;

@property (strong, nonatomic) UIButton *blackBtn;

@end

@implementation ZQSettingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.settingArray = @[@"检查更新",@"关于我们",@"空气质量等级参考"];
    // Do any additional setup after loading the view from its nib.
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    AppDelegate *app = (AppDelegate *)[UIApplication sharedApplication].delegate;
    app.revealSideViewController.panInteractionsWhenClosed =PPRevealSideInteractionNone;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.settingArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] ;
    }
    cell.textLabel.text = [self.settingArray objectAtIndex:indexPath.row];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    NSLog(@"----%@",cell.textLabel.text);
    switch (indexPath.row) {
        case 0:
        {
            [self checkVersionWithType:@"version"];
        }
            break;
        case 1:
            [self checkVersionWithType:@"about"];
            break;
        case 2:
            [self checkVersionWithType:@"levelInfo"];
            break;
        default:
            break;
    }
}

- (void)checkVersionWithType:(NSString *)type
{
    if (self.blackBtn == nil) {
        self.blackBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        self.blackBtn.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.5];
        self.blackBtn.frame = self.view.bounds;
        [self.blackBtn addTarget:self action:@selector(dissapperContentView) forControlEvents:UIControlEventTouchUpInside];
        [self.view addSubview:self.blackBtn];
        VersionView *versionView = [VersionView createViewWithType:type andData:nil];
        versionView.frame = CGRectMake(self.view.bounds.size.width/2-143, 180, 286, 290);
        if ([type isEqualToString:@"levelInfo"]) {
            versionView.frame = CGRectMake(self.view.bounds.size.width/2-143, 30, 286, self.view.frame.size.height - 79);
        }
        [self.blackBtn addSubview:versionView];
    }
}

- (void)dissapperContentView
{
    [self.blackBtn removeFromSuperview];
    self.blackBtn = nil;
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
