//
//  TNBlockAlertController.m
//  AlertControllerTest
//
//  Created by Xiao on 10/8/15.
//  Copyright © 2015 Perkinelmer. All rights reserved.
//
#import "TNBlockAlertController.h"

@interface TNBlockAlertController()<UIPopoverPresentationControllerDelegate,UIActionSheetDelegate>

@property (nonatomic, strong) NSMutableDictionary *buttonBlocks;
//@property (nonatomic, weak) id<UIPopoverPresentationControllerDelegate>presentDelegate;
@property (nonatomic, strong)UIViewController *presentDelegate;
@property (nonatomic, assign) NSInteger currentStyle;
@end
@implementation TNBlockAlertController

- (nullable instancetype)initWithTitle:(nullable NSString *)title message:(nullable NSString *)message delegate:(nullable id)delegate cancelButtonTitle:(nullable NSString *)cancelButtonTitle destructiveButtonTitle:(nullable NSString *)destructiveButtonTitle otherActions:(nullable NSArray*)actionArray AlertStyle:(OSPAlertStyle)viewStyle
{
    if (self = [super init]) {
        self.currentStyle = viewStyle;
        NSInteger systemVersion = [[UIDevice currentDevice]systemVersion].integerValue;
        if (delegate) {
//            __weak typeof(UIViewController *) weakRef = delegate;
            self.presentDelegate = delegate;
        }
        if (systemVersion>=8.0) {
            [self initViewControllerWithTitle:title message:message delegate:delegate cancelButtonTitle:cancelButtonTitle destructiveButtonTitle:destructiveButtonTitle otherActions:actionArray AlertStyle:viewStyle];
        }
        #if __IPHONE_OS_VERSION_MIN_REQUIRED < __IPHONE_8_0
        
        else{
            [self initialViewWithTitle:title message:message delegate:delegate cancelButtonTitle:cancelButtonTitle destructiveButtonTitle:destructiveButtonTitle otherActions:actionArray AlertStyle:viewStyle];
        }
        
        #endif
    }
    return self;
}
// later 8.0
- (void)initViewControllerWithTitle:(nullable NSString *)title message:(nullable NSString *)message delegate:(nullable id)delegate cancelButtonTitle:(nullable NSString *)cancelButtonTitle destructiveButtonTitle:(nullable NSString *)destructiveButtonTitle otherActions:(nonnull NSArray*)actionArray AlertStyle:(OSPAlertStyle)viewStyle
{
    switch (viewStyle) {
        case 0:
            self.alertController = [UIAlertController alertControllerWithTitle:title message:message preferredStyle:UIAlertControllerStyleAlert];
            break;
        case 1:
            self.alertController = [UIAlertController alertControllerWithTitle:title message:message preferredStyle:UIAlertControllerStyleActionSheet];
            break;
        default:
            break;
    }
    
    if (cancelButtonTitle) {
        UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:cancelButtonTitle style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
                TNAlertViewBlock block = [self alertViewblockAtButtonIndex:0];
                if (block) {
                    block();
                }
            }];
        [self.alertController addAction:cancelAction];
    }
    if (destructiveButtonTitle && viewStyle == OSPActionSheet) {
        UIAlertAction *destructiveAction = [UIAlertAction actionWithTitle:destructiveButtonTitle style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
            TNAlertViewBlock block = [self alertViewblockAtButtonIndex:1];
            if (block) {
                block();
            }
        }];
        [self.alertController addAction:destructiveAction];
    }
    for (int i=0;i<actionArray.count;i++) {
        NSString *actionTitle = [actionArray objectAtIndex:i];
        UIAlertAction *action = [UIAlertAction actionWithTitle:actionTitle style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            NSInteger buttonIndex = i;
            if (cancelButtonTitle) {
                buttonIndex=i+1;
            }
            if (destructiveButtonTitle) {
                buttonIndex+=1;
            }
            TNAlertViewBlock block = [self alertViewblockAtButtonIndex:buttonIndex];
            if (block) {
                block();
            }
        }];
        [self.alertController addAction:action];
    }
}
#if __IPHONE_OS_VERSION_MIN_REQUIRED < __IPHONE_8_0
//7.0
- (void)initialViewWithTitle:(nullable NSString *)title message:(nullable NSString *)message delegate:(nullable id)delegate cancelButtonTitle:(nullable NSString *)cancelButtonTitle destructiveButtonTitle:(nullable NSString *)destructiveButtonTitle otherActions:(nonnull NSArray*)actionArray AlertStyle:(OSPAlertStyle)viewStyle
{
    switch (viewStyle) {
        case 0:
        {
            self.alertView = [[NTNBlockAlertView alloc]initWithTitle:title message:message delegate:nil cancelButtonTitle:cancelButtonTitle otherButtonTitles:nil, nil];
            for (int i=0; i<actionArray.count; i++) {
                NSString *titleString = [actionArray objectAtIndex:i];
                [self.alertView addButtonWithTitle:titleString];
            }
        }
            break;
        case 1:
        {
            self.actionSheetView = [[NTNBlockActionSheet alloc]initWithTitle:title delegate:self cancelButtonTitle:cancelButtonTitle destructiveButtonTitle:destructiveButtonTitle otherButtonTitles:nil, nil];
            for (int i=0; i<actionArray.count; i++) {
                NSString *titleString = [actionArray objectAtIndex:i];
                [self.actionSheetView addButtonWithTitle:titleString];
            }
        }
            break;
        default:
            break;
    }

}
#endif

#pragma -mark  ---block
- (void)setBlockForOk:(nullable TNAlertViewBlock)blockForOk
{
    [self setBlock:blockForOk atButtonIndex:1];
}

- (nullable TNAlertViewBlock)blockForOk
{
    return [[self buttonBlocks] objectForKey:@1];
}

- (void)setBlockForCancel:(nullable TNAlertViewBlock)blockForCancel
{
    [self setBlock:blockForCancel atButtonIndex:0];
}

- (void)setBlockForDestructive:(nullable TNAlertViewBlock)blockForDestructive
{
    [self setBlock:blockForDestructive atButtonIndex:1];
}

- (nullable TNAlertViewBlock)blockForCancel
{
    return [self.buttonBlocks objectForKey:@0];
}

- (nullable TNAlertViewBlock)blockForDestructive
{
    return [self.buttonBlocks objectForKey:@1];
}

- (nullable TNAlertViewBlock)alertViewblockAtButtonIndex:(NSInteger)buttonIndex
{
    return [self.buttonBlocks objectForKey:[NSNumber numberWithInteger:buttonIndex]];
}

- (void)setBlock:(nullable TNAlertViewBlock)block atButtonIndex:(NSInteger)buttonIndex
{
    if (!self.buttonBlocks) {
        self.buttonBlocks = [NSMutableDictionary dictionary];
    }
    
    if (block) {
        TNAlertViewBlock temp = [block copy];
        [self.buttonBlocks setObject:temp forKey:[NSNumber numberWithInteger:buttonIndex]];
    }
}
#if __IPHONE_OS_VERSION_MIN_REQUIRED < __IPHONE_8_0
- (void)setActionSheetBlock:(nullable TNActionSheetBlock)block atButtonIndex:(NSInteger)buttonIndex
{
    if (!self.buttonBlocks) {
        self.buttonBlocks = [NSMutableDictionary dictionary];
    }
    
    TNActionSheetBlock p = [block copy];
    [self.buttonBlocks setObject:p forKey:@(buttonIndex)];
}
#endif
//-----------
#pragma -mark show
- (void)showFromDismiss:(BOOL)flag
{
    UIViewController *viewC  = [self getTopViewController];
    NSInteger systemVersion = [[UIDevice currentDevice]systemVersion].integerValue;
    if (systemVersion>=8.0){
        if(self.currentStyle == OSPActionSheet){
            UIPopoverPresentationController *ppc = self.alertController.popoverPresentationController;
            ppc.delegate = self;
            ppc.sourceView = viewC.view;
            ppc.permittedArrowDirections = 0;
            ppc.sourceRect = CGRectMake((CGRectGetWidth(ppc.sourceView.bounds)-2)*0.5f, (CGRectGetHeight(ppc.sourceView.bounds)-2)*0.5f, 2, 2);
        }
        if (viewC.presentedViewController == nil||[viewC isMemberOfClass:[UIViewController class]] || flag)
        {
            [viewC presentViewController:self.alertController animated:YES completion:nil];
        }
        else if([viewC.presentedViewController isKindOfClass:[UIViewController class]]){ [viewC.presentedViewController presentViewController:self.alertController animated:YES completion:nil];
        }
    }
#if __IPHONE_OS_VERSION_MIN_REQUIRED < __IPHONE_8_0
    else {
        if (self.currentStyle == OSPAlertView) {
            self.alertView.buttonBlocks = self.buttonBlocks;
            [self.alertView show];
        }
        else{
            self.actionSheetView.buttonBlocks = self.buttonBlocks;
            [self.actionSheetView showInView:viewC.view];
        }
    }
#endif

}

- (void)show
{
    [self showFromDismiss:NO];
}

- (UIViewController *)getTopViewController
{
    UIViewController *topVC = nil;
    if ([self.presentDelegate isKindOfClass:[UIViewController class]]) {
        topVC = (UIViewController *)self.presentDelegate;
    }else if([self.presentDelegate isKindOfClass:[UIView class]]){
        UIView *curView = (UIView *)self.presentDelegate;
        for (UIView* next = [curView superview]; next; next = next.superview) {
            UIResponder* nextResponder = [next nextResponder];
            if ([nextResponder isKindOfClass:[UIViewController class]]) {
                topVC = (UIViewController*)nextResponder;
            }  
        }
    }else{
        UIWindow * window = [[UIApplication sharedApplication] keyWindow];
        if (window.windowLevel != UIWindowLevelNormal){
            NSArray *windows = [[UIApplication sharedApplication] windows];
            for(UIWindow * tmpWin in windows)
            {
                if (tmpWin.windowLevel == UIWindowLevelNormal)
                {
                    window = tmpWin;
                    break;
                }
            }
        }
        
        UIView *frontView = [[window subviews] objectAtIndex:0];
        id nextResponder = [frontView nextResponder];
        
        if ([nextResponder isKindOfClass:[UIViewController class]]){
            if ([nextResponder isKindOfClass:[UISplitViewController class]]) {
                UISplitViewController *viewVC = (UISplitViewController *)nextResponder;
                UITabBarController *tabBar = [viewVC viewControllers][0];
                UINavigationController *nav = (UINavigationController *)tabBar.selectedViewController;
                topVC = nav.viewControllers.lastObject;
            }
            else
                topVC = nextResponder;
        }
        else
            topVC = window.rootViewController;
    }
    return topVC;
}

- (void)showInView:(nonnull UIView *)view withDirection:(UIPopoverArrowDirection)direction
{
    UIViewController *viewC = [self getTopViewController];
    UIPopoverPresentationController *ppc = self.alertController.popoverPresentationController;
    ppc.delegate = self;
    ppc.sourceView = view;
    ppc.permittedArrowDirections = direction;
    ppc.sourceRect = [self correctOriginOrderDirection:direction inView:view];
    if (viewC.presentedViewController == nil) {
        [viewC presentViewController:self.alertController animated:YES completion:nil];
    }
}

- (void)showFromBarButtonItem:(nonnull UIBarButtonItem *)item animated:(BOOL)animated
{
    UIViewController *viewC = [self getTopViewController];
    UIPopoverPresentationController *ppc = self.alertController.popoverPresentationController;
    ppc.delegate = self;
    ppc.barButtonItem = item;
    if (viewC.presentedViewController == nil) {
        [viewC presentViewController:self.alertController animated:YES completion:nil];
    }
}
- (void)showFromRect:(CGRect)rect inView:(nonnull UIView *)view animated:(BOOL)animated
{
    UIViewController *viewC = [self getTopViewController];
    UIPopoverPresentationController *ppc = self.alertController.popoverPresentationController;
    ppc.delegate = self;
    ppc.sourceView = view;
    ppc.sourceRect = rect;
    if (viewC.presentedViewController == nil) {
        [viewC presentViewController:self.alertController animated:YES completion:nil];
    }
}

- (CGRect)correctOriginOrderDirection:(UIPopoverArrowDirection)direction inView:(UIView *)view
{
    CGRect rect = CGRectZero;
    switch (direction) {
        case UIPopoverArrowDirectionUp:
            rect = CGRectMake((CGRectGetWidth(view.bounds)-2)*0.5f, (CGRectGetHeight(view.bounds)-2), 2, 2);
            break;
        case UIPopoverArrowDirectionDown:
            rect = CGRectMake((CGRectGetWidth(view.bounds)-2)*0.5f, 0, 2, 2);
            break;
        case UIPopoverArrowDirectionLeft:
            rect = CGRectMake((CGRectGetWidth(view.bounds)-2), (CGRectGetHeight(view.bounds)-2)*0.5f, 2, 2);
            break;
        case UIPopoverArrowDirectionRight:
            rect = CGRectMake(0, (CGRectGetHeight(view.bounds)-2)*0.5f, 2, 2);
            break;
        default:
            break;
    }
    return rect;
}


#pragma mark - UIPopoverPresentationControllerDelegate
- (void)prepareForPopoverPresentation:(UIPopoverPresentationController *)popoverPresentationController {
    NSLog(@"%s", __PRETTY_FUNCTION__);
}

// Called on the delegate when the popover controller will dismiss the popover. Return NO to prevent the
// dismissal of the view.
- (BOOL)popoverPresentationControllerShouldDismissPopover:(UIPopoverPresentationController *)popoverPresentationController {
    NSLog(@"%s", __PRETTY_FUNCTION__);
    return YES;
}

// Called on the delegate when the user has taken action to dismiss the popover. This is not called when the popover is dimissed programatically.
- (void)popoverPresentationControllerDidDismissPopover:(UIPopoverPresentationController *)popoverPresentationController {
    NSLog(@"%s", __PRETTY_FUNCTION__);
}

// -popoverPresentationController:willRepositionPopoverToRect:inView: is called on your delegate when the
// popover may require a different view or rectangle.
- (void)popoverPresentationController:(UIPopoverPresentationController *)popoverPresentationController willRepositionPopoverToRect:(inout CGRect *)rect inView:(inout UIView **)view {
    NSLog(@"%s", __PRETTY_FUNCTION__);
    *rect = CGRectMake((CGRectGetWidth((*view).bounds)-2)*0.5f, (CGRectGetHeight((*view).bounds)-2)*0.5f, 2, 2);// Displayed in the center position
}
@end

#if __IPHONE_OS_VERSION_MIN_REQUIRED < __IPHONE_8_0

@interface NTNBlockAlertView ()<UIAlertViewDelegate>
@property (nonatomic, weak) id<UIAlertViewDelegate> otherDelegate;
@end

@implementation NTNBlockAlertView

- (void)show{
    
    self.otherDelegate = self.delegate;
    self.delegate = self;
    [super show];
}
- (nullable TNAlertViewBlock)blockAtButtonIndex:(NSInteger)buttonIndex
{
    return [self.buttonBlocks objectForKey:[NSNumber numberWithInteger:buttonIndex]];
}

#pragma mark - UIAlertViewDelegate

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    TNAlertViewBlock block = [self blockAtButtonIndex:buttonIndex];
    if (block) {
        block();
    }
    if ([self.otherDelegate respondsToSelector:@selector(alertView:clickedButtonAtIndex:)]) {
        [self.otherDelegate alertView:alertView clickedButtonAtIndex:buttonIndex];
    }
}

 - (void)willPresentAlertView:(UIAlertView *)alertView
 {
     if (self.title == nil) {
         self.title = @"";
     }
     if ([self.otherDelegate respondsToSelector:@selector(willPresentAlertView:)]) {
         [self.otherDelegate willPresentAlertView:alertView];
     }
 }
 
 - (void)didPresentAlertView:(UIAlertView *)alertView
 {
     if ([self.otherDelegate respondsToSelector:@selector(didPresentAlertView:)]) {
         [self.otherDelegate didPresentAlertView:alertView];
     }
 }
 
 - (void)alertView:(UIAlertView *)alertView willDismissWithButtonIndex:(NSInteger)buttonIndex
 {
     if ([self.otherDelegate respondsToSelector:@selector(alertView:willDismissWithButtonIndex:)]) {
         [self.otherDelegate alertView:alertView willDismissWithButtonIndex:buttonIndex];
     }
 }
 
 - (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
 {
     if ([self.otherDelegate respondsToSelector:@selector(alertView:didDismissWithButtonIndex:)]) {
         [self.otherDelegate alertView:alertView didDismissWithButtonIndex:buttonIndex];
     }
 }
 
 // Called after edits in any of the default fields added by the style
 - (BOOL)alertViewShouldEnableFirstOtherButton:(UIAlertView *)alertView
 {
     if ([self.otherDelegate respondsToSelector:@selector(alertViewShouldEnableFirstOtherButton:)]) {
         return [self.otherDelegate alertViewShouldEnableFirstOtherButton:alertView];
     }
     return YES;
 }

@end

@interface NTNBlockActionSheet ()<UIActionSheetDelegate>

@property (nonatomic, weak) id<UIActionSheetDelegate> otherDelegate;

@end

@implementation NTNBlockActionSheet

- (void)prepareForShowing
{
    if (self.delegate != self) {
        self.otherDelegate = self.delegate;
        self.delegate = self;
    }
}

- (void)dealloc
{
    self.delegate = nil;
}

#pragma mark - Showing
/*
- (void)showInView:(UIView *)view
{
    [self prepareForShowing];
    [super showInView:view];
}

- (void)showFromBarButtonItem:(UIBarButtonItem *)item animated:(BOOL)animated
{
    [self prepareForShowing];
    [super showFromBarButtonItem:item animated:animated];
}

- (void)showFromRect:(CGRect)rect inView:(UIView *)view animated:(BOOL)animated
{
    [self prepareForShowing];
    [super showFromRect:rect inView:view animated:animated];
}

- (void)showFromTabBar:(UITabBar *)view
{
    [self prepareForShowing];
    [super showFromTabBar:view];
}

- (void)showFromToolbar:(UIToolbar *)view
{
    [self prepareForShowing];
    [super showFromToolbar:view];
}
*/
- (nullable TNActionSheetBlock)blockAtButtonIndex:(NSInteger)buttonIndex
{
    return [self.buttonBlocks objectForKey:[NSNumber numberWithInteger:buttonIndex]];
}
#pragma mark - UIActionSheetDelegate
// Called when a button is clicked. The view will be automatically dismissed after this call returns
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    TNActionSheetBlock block = [self blockAtButtonIndex:buttonIndex];
    if (block) {
        __weak typeof(actionSheet) weakRef = actionSheet;
        block(weakRef, buttonIndex);
    }
    
    if ([self.otherDelegate respondsToSelector:@selector(actionSheet:clickedButtonAtIndex:)]) {
        [self.otherDelegate actionSheet:actionSheet clickedButtonAtIndex:buttonIndex];
    }
}

// Called when we cancel a view (eg. the user clicks the Home button). This is not called when the user clicks the cancel button.
// If not defined in the delegate, we simulate a click in the cancel button
- (void)actionSheetCancel:(UIActionSheet *)actionSheet
{
    if ([self.otherDelegate respondsToSelector:@selector(actionSheetCancel:)]) {
        [self.otherDelegate actionSheetCancel:actionSheet];
    }
    
    if (self.dismissionBlock) {
        __weak typeof(actionSheet) weakRef = actionSheet;
        self.dismissionBlock(weakRef, NSNotFound);
    }
}

- (void)willPresentActionSheet:(UIActionSheet *)actionSheet  // before animation and showing view
{
    if ([self.otherDelegate respondsToSelector:@selector(willPresentActionSheet:)]) {
        [self.otherDelegate willPresentActionSheet:actionSheet];
    }
}

- (void)didPresentActionSheet:(UIActionSheet *)actionSheet  // after animation
{
    if ([self.otherDelegate respondsToSelector:@selector(didPresentActionSheet:)]) {
        [self.otherDelegate didPresentActionSheet:actionSheet];
    }
}

- (void)actionSheet:(UIActionSheet *)actionSheet willDismissWithButtonIndex:(NSInteger)buttonIndex // before animation and hiding view
{
    if ([self.otherDelegate respondsToSelector:@selector(actionSheet:willDismissWithButtonIndex:)]) {
        [self.otherDelegate actionSheet:actionSheet willDismissWithButtonIndex:buttonIndex];
    }
}

- (void)actionSheet:(UIActionSheet *)actionSheet didDismissWithButtonIndex:(NSInteger)buttonIndex  // after animation
{
    if ([self.otherDelegate respondsToSelector:@selector(actionSheet:didDismissWithButtonIndex:)]) {
        [self.otherDelegate actionSheet:actionSheet didDismissWithButtonIndex:buttonIndex];
    }
    
    if (self.dismissionBlock) {
        __weak typeof(actionSheet) weakRef = actionSheet;
        self.dismissionBlock(weakRef, buttonIndex);
    }
}

#pragma mark - Utils
- (NSInteger)addButtonWithTitle:(NSString *)title action:(TNActionSheetBlock)action
{
    NSInteger index = [super addButtonWithTitle:title];
    [self setActionSheetBlock:action atButtonIndex:index];
    return index;
}
#pragma mark - Block
- (void)setActionSheetBlock:(TNActionSheetBlock)block atButtonIndex:(NSInteger)buttonIndex
{
    if (!self.buttonBlocks) {
        self.buttonBlocks = [NSMutableDictionary dictionary];
    }
    
    TNActionSheetBlock p = [block copy];
    [self.buttonBlocks setObject:p forKey:@(buttonIndex)];
}
@end
#endif