# ImageCarousel
Android广告轮播

先看设计图（凑合看效果图吧，哈）：

![1](https://github.com/wuqingsen/Plan/blob/master/gif/20181218_140503%5B00-00-00--00-00-14%5D.gif)

具体的使用参考博客：
https://blog.csdn.net/wuqingsen1/article/details/85065845

**使用方法**

```
        //urlList为图片列表。
        imageSeller.setImageResources(urlList, new ImageCycleViewAppAdsFindSellerNew.ImageCycleViewListener() {
            @Override
            public void displayImage(String imageURL, final ImageView imageView) {
                try {
                    //加载错误图片
                    Picasso.with(MainActivity.this).load(imageURL)
                            .error(R.drawable.ic_launcher_background).into(imageView);
                } catch (Exception e) {
                }
            }

            @Override
            public void onImageClick(int position, View imageView) {
                //点击事件
                Toast.makeText(MainActivity.this, "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
```