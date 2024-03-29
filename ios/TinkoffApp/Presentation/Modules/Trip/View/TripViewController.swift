//
//  TripViewController.swift
//  TinkoffApp
//
//  Created by Станислава on 08.02.2024.
//

import UIKit

class TripViewController: UIViewController {
    
    private var output: TripViewOutput
    
    private lazy var scrollView = UIScrollView()
    
    private lazy var closeButton = UIButton(type: .system)
    private lazy var leftStarView = UIImageView()
    private lazy var rightStarView = UIImageView()
    private lazy var ratingLabel = UILabel()
    private lazy var ratingBackgroundView = UIView()
    
    private lazy var maxSpeedLabel = UILabel()
    private lazy var averageSpeedLabel = UILabel()
    
    private lazy var nighttimeLabel = UILabel()
    private lazy var lightnighttimeLabel = UILabel()
    
    private lazy var overSpeedTitle = UILabel()
    private lazy var overSpeedBackgroundView = UIView()
    
    private lazy var speedBackgroundView = UIView()
    private lazy var speedTitleLabel = UILabel()
    
    private lazy var weatherBackgroundView = UIView()
    private lazy var weatherTitleLabel = UILabel()
    
    private lazy var timeBackgroundView = UIView()
    private lazy var timeTitleLabel = UILabel()
    
    private lazy var dangerousDrivingView = UIView()
    private lazy var dangerousDrivingLabel = UILabel()
    
    private lazy var changeRoleButton = UIButton()

    private lazy var dateCityLabel = UILabel()
    
    private lazy var overSpeedTableView = UITableView()
    private lazy var overSdeeds: [SpeedModel] = []
    
    private lazy var weatherTableView = UITableView()
    private lazy var weather: [WeatherModel] = []
    
    private lazy var dangerousDrivingTableView = UITableView()
    private lazy var dangerousDriving: [DangerousDrivingModel] = []

    init(output: TripViewOutput) {
        self.output = output
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .systemGray6
        
        output.viewIsReady()
        setupView()
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        setupScrollView()
    }
    
    private func setupView() {
        view.addSubview(scrollView)

        setupScrollView()
        setupCloseButton()
        setupRating()
        setupDateCityLabel()
        setupSpeed()
        setupTime()
        setupWeather()
        setupOverSpeedTableView()
        setupDangerousDriving()
        setupChangeRoleButton()
        
        if weather.count == 0 {
            weatherBackgroundView.heightAnchor.constraint(equalToConstant: 0).isActive = true
            weatherBackgroundView.topAnchor.constraint(equalTo: timeBackgroundView.bottomAnchor, constant: 1).isActive = true
        }
        if overSdeeds.count == 0 {
            overSpeedBackgroundView.heightAnchor.constraint(equalToConstant: 0).isActive = true
            overSpeedBackgroundView.topAnchor.constraint(equalTo: weatherBackgroundView.bottomAnchor, constant: 1).isActive = true
        }
        if dangerousDriving.count == 0 {
            dangerousDrivingView.heightAnchor.constraint(equalToConstant: 0).isActive = true
            dangerousDrivingView.topAnchor.constraint(equalTo: weatherBackgroundView.bottomAnchor).isActive = true
        }
    }
    
    private func setupDateCityLabel() {
        scrollView.addSubview(dateCityLabel)
        dateCityLabel.translatesAutoresizingMaskIntoConstraints = false
        dateCityLabel.textColor = .black
        dateCityLabel.font = UIFont.systemFont(ofSize: 20, weight: .semibold)
        
        NSLayoutConstraint.activate([
            dateCityLabel.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            dateCityLabel.topAnchor.constraint(equalTo: ratingLabel.bottomAnchor, constant: 10)
        ])
    }
    
    private func setupScrollView() {
        scrollView.translatesAutoresizingMaskIntoConstraints = false
        
        NSLayoutConstraint.activate([
            scrollView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            scrollView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            scrollView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            scrollView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
        
        let height = changeRoleButton.frame.maxY + 40
        
        scrollView.contentSize = CGSize(width: scrollView.frame.width, height: height)
        scrollView.isScrollEnabled = true
    }
    
    private func setupSpeed() {
        createBackgroundView(backgroundView: speedBackgroundView, titleLabel: speedTitleLabel)
        
        speedTitleLabel.text = "Скорость"
        
        NSLayoutConstraint.activate([
            speedBackgroundView.topAnchor.constraint(equalTo: dateCityLabel.bottomAnchor, constant: 20)
        ])
        
        let maxSpeedView = UIView()
        let averageSpeedView = UIView()
        let maxSpeedTitleLabel = UILabel()
        let averageSpeedTitleLabel = UILabel()

        speedBackgroundView.addSubview(maxSpeedView)
        speedBackgroundView.addSubview(averageSpeedView)
        speedBackgroundView.addSubview(maxSpeedTitleLabel)
        speedBackgroundView.addSubview(averageSpeedTitleLabel)

        createSpeedView(speedView: maxSpeedView, speedLabel: maxSpeedLabel, titleLabel: maxSpeedTitleLabel)
        createSpeedView(speedView: averageSpeedView, speedLabel: averageSpeedLabel, titleLabel: averageSpeedTitleLabel)

        NSLayoutConstraint.activate([
            maxSpeedTitleLabel.leadingAnchor.constraint(equalTo: speedBackgroundView.leadingAnchor, constant: 20),
            maxSpeedTitleLabel.topAnchor.constraint(equalTo: speedTitleLabel.bottomAnchor, constant: 40),
            averageSpeedTitleLabel.leadingAnchor.constraint(equalTo: speedBackgroundView.centerXAnchor, constant: 0),
            averageSpeedTitleLabel.topAnchor.constraint(equalTo: speedTitleLabel.bottomAnchor, constant: 40),
            speedBackgroundView.bottomAnchor.constraint(equalTo: maxSpeedView.bottomAnchor, constant: 20)
        ])

        maxSpeedTitleLabel.text = "Макс."
        averageSpeedTitleLabel.text = "Средняя"
    }
    
    private func createSpeedView(speedView: UIView, speedLabel: UILabel, titleLabel: UILabel) {
        speedView.translatesAutoresizingMaskIntoConstraints = false
        speedLabel.translatesAutoresizingMaskIntoConstraints = false
        titleLabel.translatesAutoresizingMaskIntoConstraints = false
        
        speedView.addSubview(speedLabel)
        speedView.backgroundColor = .white
        speedView.layer.cornerRadius = 30
        speedView.layer.borderWidth = 5
        speedView.layer.borderColor = Colors.yellow.uiColor.cgColor
        speedLabel.textColor = .black
        speedLabel.font = .systemFont(ofSize: 20, weight: .bold)
        speedLabel.sizeToFit()
        titleLabel.textColor = .black
        titleLabel.font = .systemFont(ofSize: 17, weight: .semibold)
        
        NSLayoutConstraint.activate([
            speedView.heightAnchor.constraint(equalToConstant: 60),
            speedView.widthAnchor.constraint(equalToConstant: 60),
            speedView.leadingAnchor.constraint(equalTo: titleLabel.trailingAnchor, constant: 10),
            speedView.centerYAnchor.constraint(equalTo: titleLabel.centerYAnchor),
            speedLabel.centerXAnchor.constraint(equalTo: speedView.centerXAnchor),
            speedLabel.centerYAnchor.constraint(equalTo: speedView.centerYAnchor)
        ])
    }
    
    private func setupTime() {
        createBackgroundView(backgroundView: timeBackgroundView, titleLabel: timeTitleLabel)
        
        timeTitleLabel.text = "Время вождения"
        
        NSLayoutConstraint.activate([
            timeBackgroundView.topAnchor.constraint(equalTo: speedBackgroundView.bottomAnchor, constant: 20)
        ])
        
        let nighttimeTitleLabel = UILabel()
        let lightnighttimeTitleLabel = UILabel()
        
        timeBackgroundView.addSubview(nighttimeLabel)
        timeBackgroundView.addSubview(lightnighttimeLabel)
        timeBackgroundView.addSubview(nighttimeTitleLabel)
        timeBackgroundView.addSubview(lightnighttimeTitleLabel)
        
        createTimeView(timeLabel: nighttimeLabel, titleLabel: nighttimeTitleLabel)
        createTimeView(timeLabel: lightnighttimeLabel, titleLabel: lightnighttimeTitleLabel)
        
        NSLayoutConstraint.activate([
            nighttimeTitleLabel.leadingAnchor.constraint(equalTo: timeBackgroundView.leadingAnchor, constant: 30),
            nighttimeTitleLabel.topAnchor.constraint(equalTo: timeTitleLabel.bottomAnchor, constant: 30),
            lightnighttimeTitleLabel.leadingAnchor.constraint(equalTo: timeBackgroundView.centerXAnchor, constant: 0),
            lightnighttimeTitleLabel.topAnchor.constraint(equalTo: timeTitleLabel.bottomAnchor, constant: 30),
            timeBackgroundView.bottomAnchor.constraint(equalTo: nighttimeLabel.bottomAnchor, constant: 20)
        ])
        
        nighttimeTitleLabel.text = "Ночь"
        lightnighttimeTitleLabel.text = "Сумерки"
    }
    
    private func createTimeView(timeLabel: UILabel, titleLabel: UILabel) {
        timeLabel.translatesAutoresizingMaskIntoConstraints = false
        titleLabel.translatesAutoresizingMaskIntoConstraints = false

        timeLabel.textColor = .black
        timeLabel.font = .systemFont(ofSize: 24, weight: .bold)
        timeLabel.sizeToFit()
        titleLabel.textColor = .black
        titleLabel.font = .systemFont(ofSize: 17, weight: .semibold)
        NSLayoutConstraint.activate([
            timeLabel.leadingAnchor.constraint(equalTo: titleLabel.trailingAnchor, constant: 10),
            timeLabel.centerYAnchor.constraint(equalTo: titleLabel.centerYAnchor),
        ])
    }
    
    private func setupWeather() {
        weatherTableView.register(WeatherCell.self, forCellReuseIdentifier: "WeatherCell")
        
        createBackgroundView(backgroundView: weatherBackgroundView, titleLabel: weatherTitleLabel, tableView: weatherTableView)
        
        weatherTitleLabel.text = "Погодные условия"
        
        NSLayoutConstraint.activate([
            weatherBackgroundView.topAnchor.constraint(equalTo: timeBackgroundView.bottomAnchor, constant: 20),
            weatherTableView.heightAnchor.constraint(equalToConstant: CGFloat(Double(weather.count * 50) - 0.5)),
            weatherBackgroundView.heightAnchor.constraint(equalTo: weatherTableView.heightAnchor, constant: 80),
            weatherBackgroundView.bottomAnchor.constraint(equalTo: weatherTableView.bottomAnchor, constant: 20),
        ])
    }
    
    private func setupOverSpeedTableView() {
        overSpeedTableView.register(SpeedCell.self, forCellReuseIdentifier: "SpeedCell")
        
        createBackgroundView(backgroundView: overSpeedBackgroundView, titleLabel: overSpeedTitle, tableView: overSpeedTableView)
                
        NSLayoutConstraint.activate([
            overSpeedTableView.heightAnchor.constraint(equalToConstant: CGFloat(Double(overSdeeds.count * 70) - 0.5))
        ])
        
        overSpeedTitle.text = "Превышения скорости"
        
        NSLayoutConstraint.activate([
            overSpeedBackgroundView.heightAnchor.constraint(equalTo: overSpeedTableView.heightAnchor, constant: 80),
            overSpeedBackgroundView.bottomAnchor.constraint(equalTo: overSpeedTableView.bottomAnchor, constant: 20),
            overSpeedBackgroundView.topAnchor.constraint(equalTo: weatherBackgroundView.bottomAnchor, constant: 20)
        ])
    }
    
    private func setupDangerousDriving() {
        dangerousDrivingTableView.register(DangerousDrivingCell.self, forCellReuseIdentifier: "DangerousDrivingCell")
        
        createBackgroundView(backgroundView: dangerousDrivingView, titleLabel: dangerousDrivingLabel, tableView: dangerousDrivingTableView)
        
        dangerousDrivingLabel.text = "Опасное вождение"
        
        NSLayoutConstraint.activate([
            dangerousDrivingView.topAnchor.constraint(equalTo: overSpeedBackgroundView.bottomAnchor, constant: 20),
            dangerousDrivingTableView.heightAnchor.constraint(equalToConstant: CGFloat(Double(dangerousDriving.count * 70) - 0.5)),
            dangerousDrivingView.heightAnchor.constraint(equalTo: dangerousDrivingTableView.heightAnchor, constant: 80),
            dangerousDrivingView.bottomAnchor.constraint(equalTo: dangerousDrivingTableView.bottomAnchor, constant: 20),
        ])
    }
    
    private func createBackgroundView( backgroundView: UIView, titleLabel: UILabel, tableView: UITableView? = nil) {
        scrollView.addSubview(backgroundView)
        backgroundView.addSubview(titleLabel)
        
        if let tableView {
            backgroundView.addSubview(tableView)
            tableView.translatesAutoresizingMaskIntoConstraints = false
            tableView.delegate = self
            tableView.dataSource = self
            tableView.layer.cornerRadius = 10
            tableView.isScrollEnabled = false
            
            NSLayoutConstraint.activate([
                tableView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
                tableView.widthAnchor.constraint(equalTo: view.widthAnchor, constant: -40)
            ])
        }
        
        backgroundView.translatesAutoresizingMaskIntoConstraints = false
        titleLabel.translatesAutoresizingMaskIntoConstraints = false
        
        backgroundView.backgroundColor = .white
        backgroundView.layer.cornerRadius = 10
        
        titleLabel.font = .systemFont(ofSize: 22, weight: .bold)
        titleLabel.textColor = .black
        
        NSLayoutConstraint.activate([
            backgroundView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            backgroundView.widthAnchor.constraint(equalTo: view.widthAnchor, constant: -40),
            
            titleLabel.topAnchor.constraint(equalTo: backgroundView.topAnchor, constant: 20),
            titleLabel.leadingAnchor.constraint(equalTo: backgroundView.leadingAnchor, constant: 16)
        ])
    }
    
    
    private func setupCloseButton() {
        closeButton.translatesAutoresizingMaskIntoConstraints = false
        scrollView.addSubview(closeButton)
        
        closeButton.setTitle("Закрыть", for: .normal)
        closeButton.setTitleColor(.systemBlue, for: .normal)
        closeButton.titleLabel?.font = UIFont.systemFont(ofSize: 17)
        
        NSLayoutConstraint.activate([
            closeButton.heightAnchor.constraint(equalToConstant: 20),
            closeButton.topAnchor.constraint(equalTo: scrollView.topAnchor, constant: 17),
            closeButton.leftAnchor.constraint(equalTo: view.leftAnchor, constant: 16)
        ])
        
        closeButton.addTarget(
            self,
            action: #selector(dismissController),
            for: .touchUpInside
        )
    }
    
    @objc private func dismissController() {
        output.closeButtonTapped()
    }
    
    private func setupRating() {
        scrollView.addSubview(ratingBackgroundView)
        createStarView(starView: leftStarView)
        createStarView(starView: rightStarView)
        scrollView.addSubview(ratingLabel)
        ratingLabel.translatesAutoresizingMaskIntoConstraints = false
        
        NSLayoutConstraint.activate([
            ratingLabel.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            ratingLabel.topAnchor.constraint(equalTo: scrollView.topAnchor, constant: 50),
            leftStarView.trailingAnchor.constraint(equalTo: ratingLabel.leadingAnchor, constant: -10),
            leftStarView.centerYAnchor.constraint(equalTo: ratingLabel.centerYAnchor, constant: 0),
            rightStarView.leadingAnchor.constraint(equalTo: ratingLabel.trailingAnchor, constant: 10),
            rightStarView.centerYAnchor.constraint(equalTo: ratingLabel.centerYAnchor, constant: 0)
        ])
        
        ratingLabel.font = UIFont.systemFont(ofSize: 40, weight: .heavy)
        ratingLabel.textColor = .black
        
        ratingBackgroundView.backgroundColor = .white
        ratingBackgroundView.layer.borderColor = UIColor(rgb: "#fcdc2c")?.cgColor
        ratingBackgroundView.layer.borderWidth = 5
        ratingBackgroundView.layer.cornerRadius = 50
        ratingBackgroundView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            ratingBackgroundView.centerXAnchor.constraint(equalTo: ratingLabel.centerXAnchor),
            ratingBackgroundView.centerYAnchor.constraint(equalTo: ratingLabel.centerYAnchor),
            ratingBackgroundView.heightAnchor.constraint(equalToConstant: 100),
            ratingBackgroundView.widthAnchor.constraint(equalToConstant: 100)
        ])
        ratingBackgroundView.isHidden = true
    }
    
    private func createStarView(starView: UIImageView) {
        scrollView.addSubview(starView)
        starView.translatesAutoresizingMaskIntoConstraints = false
        starView.image = UIImage(systemName: "star.fill")
        starView.tintColor = Colors.yellow.uiColor
        
        NSLayoutConstraint.activate([
            starView.heightAnchor.constraint(equalToConstant: 30),
            starView.widthAnchor.constraint(equalToConstant: 30)
        ])
    }
    
    private func setupChangeRoleButton() {
        changeRoleButton.translatesAutoresizingMaskIntoConstraints = false
        scrollView.addSubview(changeRoleButton)
        changeRoleButton.setTitle("Сменить роль", for: .normal)
        changeRoleButton.setTitleColor(.black, for: .normal)
        changeRoleButton.titleLabel?.font = UIFont.systemFont(ofSize: 17, weight: .semibold)
        changeRoleButton.backgroundColor = Colors.yellow.uiColor
        changeRoleButton.layer.cornerRadius = 10
        
        NSLayoutConstraint.activate([
            changeRoleButton.topAnchor.constraint(equalTo: dangerousDrivingView.bottomAnchor, constant: 25),
            changeRoleButton.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            changeRoleButton.heightAnchor.constraint(equalToConstant: 40),
            changeRoleButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 20),
            changeRoleButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -20)
        ])
        
        changeRoleButton.addTarget(
            self,
            action: #selector(changeRoleButtonTapped),
            for: .touchUpInside
        )
    }
    
    @objc private func changeRoleButtonTapped() {
        showAlert()
    }
    
    private func showAlert() {
        let alertController = UIAlertController(title: "Смена роли", message: "Хотите сменить роль на Пассажир?", preferredStyle: .alert)
        
        let okAction = UIAlertAction(title: "Сменить", style: .default) { [weak self] (_) in
            self?.output.changeRoleButtonTapped()
            self?.output.closeButtonTapped()
        }
        
        let cancelAction = UIAlertAction(title: "Отмена", style: .default) { _ in }
            
        
        alertController.addAction(okAction)
        alertController.addAction(cancelAction)
        
        present(alertController, animated: true, completion: nil)
    }
}

extension TripViewController: TripViewInput {
    func setupTrip(model: TripModel) {
        ratingLabel.text = "\(model.rating)"
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd MMMM yyyy"
        let date = dateFormatter.string(from: model.date)
        dateCityLabel.text = "\(date) г."
        
        maxSpeedLabel.text = "\(model.maxSpeed)"
        averageSpeedLabel.text = "\(model.averageSpeed)"
        nighttimeLabel.text = "\(model.nightTime)%"
        lightnighttimeLabel.text = "\(model.lightNightTime)%"
        overSdeeds = model.overSdeeds
        weather = model.weather
        dangerousDriving = model.accelerations
    }
}

extension TripViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if tableView == overSpeedTableView {
            return overSdeeds.count
        } else if tableView == weatherTableView {
            return weather.count
        } else {
            return dangerousDriving.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if tableView == overSpeedTableView {
            guard let cell = tableView.dequeueReusableCell(withIdentifier: "SpeedCell", for: indexPath) as? SpeedCell else {
                fatalError("Cannot create SpeedCell")
            }
            cell.configure(with: overSdeeds[indexPath.row])
            cell.selectionStyle = .none
            return cell
        } else if tableView == weatherTableView {
            guard let cell = tableView.dequeueReusableCell(withIdentifier: "WeatherCell", for: indexPath) as? WeatherCell else {
                fatalError("Cannot create WeatherCell")
            }
            cell.configure(with: weather[indexPath.row])
            cell.selectionStyle = .none
            return cell
        } else {
            guard let cell = tableView.dequeueReusableCell(withIdentifier: "DangerousDrivingCell", for: indexPath) as? DangerousDrivingCell else {
                fatalError("Cannot create DangerousDrivingCell")
            }
            cell.configure(with: dangerousDriving[indexPath.row])
            cell.selectionStyle = .none
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if tableView == overSpeedTableView {
            return 70
        } else if tableView == weatherTableView {
            return 50
        } else {
            return 70
        }
    }
}
