---
title: Ant Design of Angular 快速上手系列
seo_title: Ant Design of Angular 快速上手系列
toc: true
indent: true
tag:
  - Angular
  - NG-ZORRO
categories: 前端
references:
  - title: Ant Design of Angular
    url: 'https://ng.ant.design/docs/introduce/zh'
date: '2020-11-21 17:10'
abbrlink: 8ab78feb
description: '一句话概括那就是：这是一份后端开发所写的前端学习内容~，面向于快速上手这种哒。'
updated: '2020-12-30 10:40'
headimg: ../../img/article/Ant_Design_of_Angular_快速上手系列/image-20210320124951090.png
icons: [fab fa-angular red]
---

<p class="p center logo large"><em>Angular 快速上手系列</em></p><br><br>

准确来说，我不是职业前端，因为需要使用 Angular 框架完成项目，所以顺带了解到了 NG-ZORRO 这套前端框架。整个过程处于边学习边使用的状态，主要的方式也是偏向于完成设计内容而非了解其实现原理。系统前端这块儿，使用的是 `Node 12.19.0` 和 `NG-ZORRO 9.3.0` ，开发软件 `WebStorm` 。

Ant Design of Angular ，也就是 *ng-zorro-antd* ，是遵循 Ant Design 设计规范的 Angular UI 组件库，全部代码开源并遵循 MIT 协议，任何企业、组织及个人均可免费使用。它的特定：提炼自企业级中后台产品的交互语言和视觉风格。开箱即用的高质量 Angular 组件，与 Angular 保持同步升级。使用 TypeScript 构建，提供完整的类型定义文件。支持 OnPush 模式，性能卓越等等。

当然，以上内容均是摘自官网，我觉得它好用的原因是：{% bb 臣妾做不到哇,我不会写前端样式，而它拥有现成的组件库 %}（坏笑 😏

## 一、公共配置

### 1. 属性基础配置

需要注意的配置点：1.全局公共属性配置，比如设定 `message` 组件的 `top` 高度，2.图标库，导入到项目中的 `icons` 图标，3.组件库，V8 版本弃用了 `NgZorroAntdModule` *(V10 彻底不支持)*，所以需要独立引入。

已知在正常使用过程中，在自己的 `component` 中引入 NG-ZORRO 的组件是需要进入导入的，重复的导入行为势必会很麻烦，所以可以将其抽成一个公共的 `modal` ，预先性的导入，再将其引入到 `AppComponent` 中的 `imports` 里，可以尽量减少一些工作量。另外，对于一些提供了公共配置的组件，通过注入令牌 `NZ_CONFIG` 提供一个 `NzConfig` 接口对象，将全局配置项注入到 `NzConfigService` 之中，在减少工作量的同时还统一项目中的各个显示效果。

*PS：全局配置优先级低于独立为组件的某个实例单独设置的值*

{% folding cyan, icons-provider.module.ts %}
{% codeblock lang:ts line_number:false icons-provider.module.ts %}
import { NgModule } from '@angular/core';
import { NZ_ICONS, NzIconModule } from 'ng-zorro-antd/icon';

import {
  MenuFoldOutline,
  MenuUnfoldOutline,
  FormOutline,
  DashboardOutline
} from '@ant-design/icons-angular/icons';

const icons = [MenuFoldOutline, MenuUnfoldOutline, DashboardOutline, FormOutline];

@NgModule({
  imports: [NzIconModule],
  exports: [NzIconModule],
  providers: [
    { provide: NZ_ICONS, useValue: icons }
  ]
})
export class IconsProviderModule {
}
{% endcodeblock %}
{% endfolding %}

{% folding cyan, ng-zorro-antd.module.ts %}
{% codeblock lang:ts line_number:false ng-zorro-antd.module.ts %}
// 已经忽略 import 部分

// NG ZORRO 全局配置文件
const ngZorroConfig: NzConfig = {
  message: {nzTop: 100},
  notification: {nzTop: 300}
};

@NgModule({
  providers: [
    {provide: NZ_CONFIG, useValue: ngZorroConfig}
  ],
  exports: [
    NzAffixModule,
    NzAlertModule,
    NzAnchorModule,
    NzAutocompleteModule,
    NzAvatarModule,
    NzBackTopModule,
    NzBadgeModule,
    NzButtonModule,
    NzBreadCrumbModule,
    NzCalendarModule,
    NzCardModule,
    NzCarouselModule,
    NzCascaderModule,
    NzCheckboxModule,
    NzCollapseModule,
    NzCommentModule,
    NzDatePickerModule,
    NzDescriptionsModule,
    NzDividerModule,
    NzDrawerModule,
    NzDropDownModule,
    NzEmptyModule,
    NzFormModule,
    NzGridModule,
    NzI18nModule,
    NzIconModule,
    NzInputModule,
    NzInputNumberModule,
    NzLayoutModule,
    NzListModule,
    NzMentionModule,
    NzMenuModule,
    NzMessageModule,
    NzModalModule,
    NzNoAnimationModule,
    NzNotificationModule,
    NzPageHeaderModule,
    NzPaginationModule,
    NzPopconfirmModule,
    NzPopoverModule,
    NzProgressModule,
    NzRadioModule,
    NzRateModule,
    NzResultModule,
    NzSelectModule,
    NzSkeletonModule,
    NzSliderModule,
    NzSpinModule,
    NzStatisticModule,
    NzStepsModule,
    NzSwitchModule,
    NzTableModule,
    NzTabsModule,
    NzTagModule,
    NzTimePickerModule,
    NzTimelineModule,
    NzToolTipModule,
    NzTransButtonModule,
    NzTransferModule,
    NzTreeModule,
    NzTreeSelectModule,
    NzTypographyModule,
    NzUploadModule,
    NzWaveModule,
    NzResizableModule,
    NzPipesModule,
  ]
})
export class NgZorroAntdCommonModule {

}
{% endcodeblock %}
{% endfolding %}

### 2. 样式覆盖配置

样式覆盖可以分成两种：全局或者各个子组件中的。全局样式覆盖需要先找到默认的样式内容，地址如下：[default.less](https://github.com/NG-ZORRO/ng-zorro-antd/blob/master/components/style/themes/default.less) 。首先我们在 `src` 目录下建立一个单独的 `theme.less` 文件，在 `angular.json` 文件的 `styles` 列表加入该文件，拷贝默认样式文件内容，按需修啊即可（比如修改公共颜色）

{% codeblock lang:json line_number:false angular.json %}
"styles": [
    "src/theme.less"
],
{% endcodeblock %}

组件中的独立样式修改就更简单了，找到对应的 `class` 名称，通过 `::ng-deep` 覆盖它！另外留意一下样式污染的可能，建议通过嵌套结构减少样式修改的范围。

{% codeblock lang:scss line_number:false some style %}
::ng-deep .ant-input-number {
    width: 100% !important;
}
{% endcodeblock %}

基础的配置大概也就以上两个部分的，其它的就是各类组件的使用了。

## 二、表单组件

表单的用途就太广泛了，注册、登录、查询、信息提交等等都需要到，本文中的所有关于表单的内容均已 [响应式表单](https://angular.io/guide/reactive-forms#reactive-forms) 展开，与之对立的 [模板驱动表单](https://angular.io/guide/forms#template-driven-forms) 不做学习。

### 1. 基础表单设定

`FormGroup` ，我们通过使用 `FormBuilder` 对象提供的 `group()` 方法，来创建 `FormGroup` 和 `FormControl` 对象，以一个简单的申请为例也就是这个样子：

{% codeblock lang:ts line_number:false %}
validatePassWdForm!: FormGroup;

constructor(private fb: FormBuilder) {
    this.validateForm = this.fb.group({
        name: [null, [Validators.required]],
        email: [null, [Validators.required, Validators.email]],
        pwd: [null, [Validators.required, this.validList.validateWeakPwd]],
        agree: [false, [Validators.requiredTrue]]
    });
}

validList = {
    validateWeakPwd: (control: FormControl): { [s: string]: boolean } => {
        const testString = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,.\/]).{8,16}$/;
        if (!control.value) {
            return {required: true};
        } else if (!testString.test(control.value)) {
            return {weakPwd: true, error: true};
        }
        return {};
    }
}
{% endcodeblock %}

可以理解为：`字段名称: [初始值，[验证器1,验证器2,...], [异步验证器1,异步验证器2,...]]`
初始值可以为空，也可以赋值，验证器这边可以使用一些默认自带的，就比如 `Validators.required` 必填校验，也可以自定义验证规则，此外还支持异步验证，接收与后端数据交互后的验证效果，比如用户名唯一性校验。而对应到页面上，字段名称与 `formControlName` 对应，按照上面例子所对应的表单应该是：

{% folding cyan, 响应式表单 %}
{% codeblock lang:html line_number:false %}

<form nz-form [formGroup]="validateForm">
    <nz-form-item>
        <nz-form-label nzFor="name">名称</nz-form-label>
        <nz-form-control nzErrorTip="请输入名称" nzHasFeedback>
        <input placeholder="请输入名称" nz-input formControlName="name" id="name"/>
        </nz-form-control>
    </nz-form-item>
    <nz-form-item>
        <nz-form-label nzFor="email">邮箱</nz-form-label>
        <nz-form-control [nzErrorTip]="template" nzHasFeedback>
        <input placeholder="请输入邮箱" nz-input formControlName="email" id="email"/>
        <ng-template #template let-control>
            <ng-container *ngIf="control.hasError('required')">请输入您的邮箱</ng-container>
            <ng-container *ngIf="control.hasError('email')">邮箱格式不正确</ng-container>
        </ng-template>
        </nz-form-control>
    </nz-form-item>
    <nz-form-item>
        <nz-form-label nzFor="pwd">密码</nz-form-label>
        <nz-form-control [nzErrorTip]="errorTplPwd" nzHasFeedback>
        <input placeholder="请输入密码" type="password" nz-input formControlName="pwd" id="pwd"/>
        <ng-template #errorTplPwd let-control>
            <ng-container *ngIf="control.hasError('required')">请输入新密码</ng-container>
            <ng-container *ngIf="control.hasError('weakPwd')">
                必须包含字母、数字、特殊字符，长度8~16位！
            </ng-container>
        </ng-template>
        </nz-form-control>
    </nz-form-item>
    <nz-form-item nz-row class="register-area">
        <nz-form-control nzErrorTip="请勾选服务协议">
            <label nz-checkbox formControlName="agree"><span>我已阅读并同意接受《服务协议》</span></label>
        </nz-form-control>
    </nz-form-item>
</form>
{% endcodeblock %}
{% endfolding %}

### 2. 表单值的赋值

除了在初始化时通过 FormBuilder 构建外，还可以独立的进入 **表单赋值** ，批量设置的可以使用 `patchValue()` 方法，单独设置可以使用 `setValue()` 方法。

{% codeblock lang:ts line_number:false 表单赋值 %}
this.validateForm.patchValue({
    name: '小明',
    email: 'xiaoming@gmail.com',
    pwd: '!QAZ2wsx',
    agree: true
})

this.validateForm.get('email').setValue('xiaoming@gmail.com');

this.validateForm.controls.pwd.setValue('!QAZ2wsx');
{% endcodeblock %}

自然而然的，重设验证器，重置表单也不在话下：

- 设置验证器：`this.validateForm.get(name).setValidators(newValidator);`
- 全局表单只读：`this.validateForm.disable();  // 启用 enable();`
- 单独字段只读：`this.validateForm.get(name).disable();`
