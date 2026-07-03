# Crypto Wealth Watcher

## პროექტის აღწერა

Crypto Wealth Watcher არის Android აპლიკაცია, რომელიც მომხმარებელს აძლევს საშუალებას ნახოს კრიპტოვალუტების მიმდინარე ფასები, დაამატოს სასურველი მონეტები ფავორიტებში და შექმნას საკუთარი პორტფოლიო.

აპლიკაცია მონაცემებს იღებს CoinGecko API-დან და აჩვენებს მათ რეალურ დროში.

---

## ძირითადი ფუნქციები

- კრიპტოვალუტების სიის ნახვა
- მიმდინარე ფასების მიღება ინტერნეტიდან
- მონეტის ფავორიტებში დამატება და წაშლა
- საკუთარი პორტფოლიოს შექმნა
- პორტფოლიოს საერთო ღირებულების გამოთვლა
- მონაცემების შენახვა SharedPreferences-ში

---

## გამოყენებული ტექნოლოგიები

- Kotlin
- XML
- MVVM Architecture
- Retrofit
- RecyclerView
- Navigation Component
- ViewBinding
- Glide
- SharedPreferences
- Material Design

---

## პროექტის სტრუქტურა

```
UI
│
├── Home
├── Favorites
└── Portfolio

ViewModel

Repository

Retrofit(API)

Model
```

---

## როგორ მუშაობს აპლიკაცია

აპლიკაციის გახსნისას ხდება CoinGecko API-დან კრიპტოვალუტების სიის მიღება.

Home ეკრანზე ჩანს ყველა მონეტა.

მონეტაზე დიდხანს დაჭერის შემთხვევაში ის ემატება ან იშლება Favorites სიიდან.

Portfolio ეკრანზე მომხმარებელს შეუძლია დაამატოს საკუთარი კრიპტო აქტივები, ხოლო აპლიკაცია ავტომატურად ითვლის მათი საერთო ღირებულებას.

---

## გამოყენებული API

CoinGecko API

https://api.coingecko.com/api/v3/

---

## პროექტის გაშვება

1. გადმოწერეთ პროექტი GitHub-დან.
2. გახსენით Android Studio-ში.
3. დაელოდეთ Gradle-ის სინქრონიზაციას.
4. გაუშვით Emulator-ზე ან Android მოწყობილობაზე.

---

## ავტორი

Lia Gelashvili

Final Project

Mobile Application Development
