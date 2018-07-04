package ishopgo.com.exhibition.app

import android.app.Application
import dagger.Component
import ishopgo.com.exhibition.domain.ApiService
import ishopgo.com.exhibition.domain.db.MessageRepository
import ishopgo.com.exhibition.ui.chat.local.ChatViewModel
import ishopgo.com.exhibition.ui.chat.local.contact.ContactViewModel
import ishopgo.com.exhibition.ui.chat.local.contact.search.SearchContactViewModel
import ishopgo.com.exhibition.ui.chat.local.conversation.ConversationViewModel
import ishopgo.com.exhibition.ui.chat.local.conversation.pattern.PatternChooserViewModel
import ishopgo.com.exhibition.ui.chat.local.group.addmember.MemberViewModel
import ishopgo.com.exhibition.ui.chat.local.imageinventory.ImageInventoryViewModel
import ishopgo.com.exhibition.ui.chat.local.inbox.InboxViewModel
import ishopgo.com.exhibition.ui.chat.local.inbox.search.SearchInboxViewModel
import ishopgo.com.exhibition.ui.chat.local.info.MemberInfoViewModel
import ishopgo.com.exhibition.ui.chat.local.profile.MemberProfileViewModel
import ishopgo.com.exhibition.ui.community.CommunityViewModel
import ishopgo.com.exhibition.ui.community.comment.CommentViewModel
import ishopgo.com.exhibition.ui.login.LoginViewModel
import ishopgo.com.exhibition.ui.main.MainViewModel
import ishopgo.com.exhibition.ui.main.account.AccountViewModel
import ishopgo.com.exhibition.ui.main.administrator.AdministratorViewModel
import ishopgo.com.exhibition.ui.main.administrator.add.FragmentAdministratorViewModel
import ishopgo.com.exhibition.ui.main.boothfollow.BoothFollowViewModel
import ishopgo.com.exhibition.ui.main.boothmanager.BoothManagerViewModel
import ishopgo.com.exhibition.ui.main.brand.popular.PopularBrandsViewModel
import ishopgo.com.exhibition.ui.main.brandmanager.BrandManagerViewModel
import ishopgo.com.exhibition.ui.main.configbooth.ConfigBoothViewModel
import ishopgo.com.exhibition.ui.main.home.HomeViewModel
import ishopgo.com.exhibition.ui.main.home.category.product.ProductsByCategoryViewModel
import ishopgo.com.exhibition.ui.main.home.introduction.IntroductionViewModel
import ishopgo.com.exhibition.ui.main.home.post.post.PostMenuViewModel
import ishopgo.com.exhibition.ui.main.home.post.question.QuestionMenuViewModel
import ishopgo.com.exhibition.ui.main.home.search.SearchViewModel
import ishopgo.com.exhibition.ui.main.home.search.community.SearchCommunityViewModel
import ishopgo.com.exhibition.ui.main.home.search.product.SearchProductViewModel
import ishopgo.com.exhibition.ui.main.home.search.sale_point.SearchSalePointViewModel
import ishopgo.com.exhibition.ui.main.home.search.shop.SearchShopsViewModel
import ishopgo.com.exhibition.ui.main.map.ExpoShopViewModel
import ishopgo.com.exhibition.ui.main.map.config.ExpoConfigViewModel
import ishopgo.com.exhibition.ui.main.membermanager.MemberManagerViewModel
import ishopgo.com.exhibition.ui.main.myqr.MyQrViewModel
import ishopgo.com.exhibition.ui.main.notification.NotificationViewModel
import ishopgo.com.exhibition.ui.main.postmanager.PostViewModel
import ishopgo.com.exhibition.ui.main.product.branded.ProductsOfBrandViewModel
import ishopgo.com.exhibition.ui.main.product.detail.ProductDetailViewModel
import ishopgo.com.exhibition.ui.main.product.detail.RatingProductViewModel
import ishopgo.com.exhibition.ui.main.product.detail.comment.ProductCommentViewModel
import ishopgo.com.exhibition.ui.main.product.detail.sale_point.ProductSalePointViewModel
import ishopgo.com.exhibition.ui.main.product.favorite.FavoriteProductsViewModel
import ishopgo.com.exhibition.ui.main.product.popular.PopularProductsViewModel
import ishopgo.com.exhibition.ui.main.product.shop.ProductsOfShopViewModel
import ishopgo.com.exhibition.ui.main.product.suggested.SuggestedProductsViewModel
import ishopgo.com.exhibition.ui.main.product.viewed.ViewedProductsViewModel
import ishopgo.com.exhibition.ui.main.productfollow.ProductFollowViewModel
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerViewModel
import ishopgo.com.exhibition.ui.main.profile.ProfileViewModel
import ishopgo.com.exhibition.ui.main.questmanager.QuestionSearchViewModel
import ishopgo.com.exhibition.ui.main.questmanager.QuestionViewModel
import ishopgo.com.exhibition.ui.main.registerbooth.RegisterBoothViewModel
import ishopgo.com.exhibition.ui.main.salepoint.SalePointViewModel
import ishopgo.com.exhibition.ui.main.salepointdetail.SalePointDetailViewModel
import ishopgo.com.exhibition.ui.main.scan.ScanViewModel
import ishopgo.com.exhibition.ui.main.shop.ShopDetailViewModel
import ishopgo.com.exhibition.ui.main.shop.category.CategoryViewModel
import ishopgo.com.exhibition.ui.main.shop.info.ShopInfoViewModel
import ishopgo.com.exhibition.ui.main.shop.news.NewsViewModel
import ishopgo.com.exhibition.ui.main.shop.rate.RateViewModel
import ishopgo.com.exhibition.ui.main.ticket.TicketViewModel
import ishopgo.com.exhibition.ui.main.ticketmanager.TicketManagerViewModel
import ishopgo.com.exhibition.ui.main.visitors.VisitorsViewModel
import ishopgo.com.exhibition.ui.splash.SplashViewModel
import ishopgo.com.exhibition.ui.survey.SurveyViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, ApiModule::class, RepositoryModule::class))
interface AppComponent {

    fun application(): Application

    fun authService(): ApiService.Auth

    fun noAuthService(): ApiService.NoAuth

    fun isgService(): ApiService.ISGApi

    fun appdb(): MessageRepository

    interface Injectable {
        fun inject(appComponent: AppComponent)
    }

    fun inject(splashViewModel: SplashViewModel)
    fun inject(main: MainViewModel)
    fun inject(productDetailViewModel: ProductDetailViewModel)
    fun inject(favoriteProductsViewModel: FavoriteProductsViewModel)
    fun inject(viewedProductsViewModel: ViewedProductsViewModel)
    fun inject(popularBrandsViewModel: PopularBrandsViewModel)
    fun inject(popularProductsViewModel: PopularProductsViewModel)
    fun inject(shopProductsViewModel: ProductsOfShopViewModel)
    fun inject(productCommentViewModel: ProductCommentViewModel)
    fun inject(shopDetailViewModel: ShopDetailViewModel)
    fun inject(shopInfoViewModel: ShopInfoViewModel)
    fun inject(categoryViewMode: CategoryViewModel)
    fun inject(accountViewModel: AccountViewModel)
    fun inject(profileViewModel: ProfileViewModel)
    fun inject(communityViewModel: CommunityViewModel)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(scanViewModel: ScanViewModel)
    fun inject(productsOfBrandViewModel: ProductsOfBrandViewModel)
    fun inject(homeViewModel: HomeViewModel)
    fun inject(searchViewModel: SearchViewModel)
    fun inject(searchProductViewModel: SearchProductViewModel)
    fun inject(searchShopHasProductsViewModel: SearchShopsViewModel)
    fun inject(productsByCategoryViewModel: ProductsByCategoryViewModel)
    fun inject(rateViewModel: RateViewModel)
    fun inject(configBoothViewModel: ConfigBoothViewModel)
    fun inject(notificationViewModel: NotificationViewModel)
    fun inject(productManagerViewModel: ProductManagerViewModel)
    fun inject(suggestedProductsViewModel: SuggestedProductsViewModel)
    fun inject(salePointViewModel: SalePointViewModel)
    fun inject(boothManagerViewModel: BoothManagerViewModel)
    fun inject(memberManagerViewModel: MemberManagerViewModel)
    fun inject(brandManagerViewModel: BrandManagerViewModel)
    fun inject(postViewModel: PostViewModel)
    fun inject(questionViewModel: QuestionViewModel)
    fun inject(questionSearchViewModel: QuestionSearchViewModel)
    fun inject(profileViewModel: MemberProfileViewModel)
    fun inject(conversationViewModel: ConversationViewModel)
    fun inject(patternChooserViewModel: PatternChooserViewModel)
    fun inject(imageInventoryViewModel: ImageInventoryViewModel)
    fun inject(memberInfoViewModel: MemberInfoViewModel)
    fun inject(memberViewModel: MemberViewModel)
    fun inject(registerBoothViewModel: RegisterBoothViewModel)
    fun inject(productSalePointViewModel: ProductSalePointViewModel)
    fun inject(myQrViewModel: MyQrViewModel)
    fun inject(searchCommunityViewModel: SearchCommunityViewModel)
    fun inject(chatViewModel: ChatViewModel)
    fun inject(inboxViewModel: InboxViewModel)
    fun inject(contactViewModel: ContactViewModel)
    fun inject(searchInboxViewModel: SearchInboxViewModel)
    fun inject(searchContactViewModel: SearchContactViewModel)
    fun inject(surveyViewModel: SurveyViewModel)
    fun inject(ticketViewModel: TicketViewModel)
    fun inject(searchSalePointViewModel: SearchSalePointViewModel)
    fun inject(salePointDetailViewModel: SalePointDetailViewModel)
    fun inject(postMenuViewModel: PostMenuViewModel)
    fun inject(questionMenuViewModel: QuestionMenuViewModel)
    fun inject(productFollowViewModel: ProductFollowViewModel)
    fun inject(boothFollowViewModel: BoothFollowViewModel)
    fun inject(ticketManagerViewModel: TicketManagerViewModel)
    fun inject(visitorsViewModel: VisitorsViewModel)
    fun inject(expoShopViewModel: ExpoShopViewModel)
    fun inject(expoConfigViewModel: ExpoConfigViewModel)
    fun inject(ratingProductViewModel: RatingProductViewModel)
    fun inject(introductionViewModel: IntroductionViewModel)
    fun inject(commentViewModel: CommentViewModel)
    fun inject(newsViewModel: NewsViewModel)
    fun inject(administratorViewModel: AdministratorViewModel)
    fun inject(fragmentAdministratorViewModel: FragmentAdministratorViewModel)
}