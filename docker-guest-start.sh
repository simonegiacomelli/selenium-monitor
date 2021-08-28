DIR=$(dirname $(readlink -f $0))
echo $DIR
cd $DIR
mkdir -p docker-guest-log
mv docker-guest.log docker-guest-log/"$(date +"old-%m-%d-%y--%T.%N")".log
date > docker-guest.log
echo ------------------------------------------------------------ | tee -a docker-guest.log
echo ------------------------------------------------------------ | tee -a docker-guest.log
echo ------------------------------------------------------------ | tee -a docker-guest.log
echo ------------------------------------------------------------ | tee -a docker-guest.log
./gradlew clean --console=plain 2>&1 | tee -a docker-guest.log
(./gradlew run --console=plain 2>&1 | tee -a docker-guest.log) &

sleep infinity

